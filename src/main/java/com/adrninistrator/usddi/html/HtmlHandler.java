package com.adrninistrator.usddi.html;

import com.adrninistrator.usddi.common.USDDIConstants;
import com.adrninistrator.usddi.common.enums.HtmlFragmentTypeEnum;
import com.adrninistrator.usddi.dto.common.Counter;
import com.adrninistrator.usddi.dto.html.FontAttributesAboutSize;
import com.adrninistrator.usddi.dto.html.HtmlFormatResult;
import com.adrninistrator.usddi.dto.html.HtmlFragment;
import com.adrninistrator.usddi.dto.html.HtmlPreFormatResult;
import com.adrninistrator.usddi.dto.html.Node4HtmlTree;
import com.adrninistrator.usddi.exceptions.HtmlFormatException;
import com.adrninistrator.usddi.logger.DebugLogger;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * @author adrninistrator
 * @date 2024/9/28
 * @description:
 */
public class HtmlHandler implements AutoCloseable {

    private final Graphics graphics;

    private final Map<String, FontMetrics> fontMetricsMap = new HashMap<>();

    public HtmlHandler() {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        graphics = img.getGraphics();
    }

    @Override
    public void close() throws Exception {
        graphics.dispose();
    }

    // 根据与大小相关的字体属性获得对应的FontMetrics对象
    private FontMetrics getFontMetrics(FontAttributesAboutSize fontAttributesAboutSize) {
        int fontStyle = fontAttributesAboutSize.isBold() ? Font.BOLD : Font.PLAIN;
        return getFontMetrics(fontAttributesAboutSize.getFontName(), fontStyle, fontAttributesAboutSize.getFontSize());
    }

    // 根据字体获得对应的FontMetrics对象
    private FontMetrics getFontMetrics(String fontName, int fontStyle, int fontSize) {
        String key = StringUtils.joinWith("\t", fontName, fontStyle, fontSize);
        FontMetrics fontMetrics = fontMetricsMap.get(key);
        if (fontMetrics == null) {
            Font font = new Font(fontName, fontStyle, fontSize);
            fontMetrics = graphics.getFontMetrics(font);
            fontMetricsMap.put(key, fontMetrics);
        }
        return fontMetrics;
    }

    /**
     * 格式化html
     *
     * @param html            原始html
     * @param allowedMaxWidth 格式化后允许的最大宽度
     * @param fontName        字体名称
     * @param fontSize        字体大小
     * @return
     */
    public HtmlFormatResult formatHtml(String html, int allowedMaxWidth, String fontName, int fontSize) throws HtmlFormatException {
        return formatHtml(html, BigDecimal.valueOf(allowedMaxWidth), fontName, fontSize);
    }

    /**
     * 格式化html
     *
     * @param html            原始html
     * @param allowedMaxWidth 格式化后允许的最大宽度
     * @param fontName        字体名称
     * @param fontSize        字体大小
     * @return
     */
    public HtmlFormatResult formatHtml(String html, BigDecimal allowedMaxWidth, String fontName, int fontSize) throws HtmlFormatException {
        DebugLogger.emptyLine();
        DebugLogger.log(this.getClass(), "格式化html", "格式化后允许的最大宽度", allowedMaxWidth.toPlainString(), "字体名称", fontName, "字体大小", fontSize, "html内容", html);
        // 解析html，预处理
        HtmlPreFormatResult htmlPreFormatResult = preFormatHtml(html, allowedMaxWidth, fontName, fontSize);
        // 处理HTML片段，拼接当前HTML元素内容
        StringBuilder formattedHtmlText = new StringBuilder();
        for (HtmlFragment htmlFragment : htmlPreFormatResult.getHtmlFragmentListReadOnly()) {
            formattedHtmlText.append(htmlFragment.getContent());
        }
        HtmlFormatResult htmlFormatResult = new HtmlFormatResult();
        htmlFormatResult.setFormattedHtmlText(formattedHtmlText.toString());
        // 处理HTML文本宽度
        htmlFormatResult.setWidth(htmlPreFormatResult.getHtmlTextMaxWidth());
        // 记录HTML文本总高度
        int totalHeight = 0;
        // 记录字体大小超过0的行的数量
        int lineNum = 0;
        for (int maxFontSize : htmlPreFormatResult.getMaxFontSizePerLineListReadOnly()) {
            if (maxFontSize == 0) {
                // 若某一行的HTML文本最大字体大小为0，说明不需要处理
                continue;
            }
            DebugLogger.log(this.getClass(), "当前行的文本最大字体大小", "行数", lineNum, "字体大小", maxFontSize);
            // 计算HTML文本高度
            totalHeight += computeHtmlTextHeight(maxFontSize);
            lineNum++;
        }
        // 所有的单个字符高度之和 + (文字行数 - 1) * 1
        htmlFormatResult.setHeight(totalHeight + lineNum - 1);
        return htmlFormatResult;
    }

    /**
     * 计算HTML文本高度
     *
     * @param verticalTotalFontSize 垂直方向HTML文本的总字体大小
     * @return
     */
    private int computeHtmlTextHeight(int verticalTotalFontSize) {
        // 字体大小乘以 1.2 后的结果四舍五入，再减 1
        double value = verticalTotalFontSize * 1.2D;
        return (int) Math.round(value) - 1;
    }

    /**
     * 解析html，预处理
     *
     * @param html            原始html
     * @param allowedMaxWidth 格式化后允许的最大宽度
     * @param fontName        字体名称
     * @param fontSize        字体大小
     * @return
     */
    private HtmlPreFormatResult preFormatHtml(String html, BigDecimal allowedMaxWidth, String fontName, int fontSize) throws HtmlFormatException {
        HtmlPreFormatResult htmlPreFormatResult = new HtmlPreFormatResult();
        Deque<Node4HtmlTree> stack = new ArrayDeque<>();
        // 记录当前行HTML文本宽度
        Counter curLineHtmlTextWidthCounter = new Counter();
        // 记录当前行HTML文本最大字体大小
        Counter curLineHtmlTextMaxFontSizeCounter = new Counter();
        Document document = Jsoup.parse(html);
        // 从body开始处理
        Node4HtmlTree node4HtmlTree = new Node4HtmlTree(document.body());
        stack.push(node4HtmlTree);

        while (!stack.isEmpty()) {
            Node4HtmlTree currentStackNode = stack.peek();
            // 尝试处理HTML节点的下一个子节点
            if (!tryHandleNextChildNode(currentStackNode, stack, htmlPreFormatResult)) {
                // 当前HTML节点的子节点已处理完毕
                continue;
            }

            // 获取当前处理的HTML节点的子节点
            Node currentChildNode = currentStackNode.getCurrentChildNode();
            if (currentChildNode instanceof TextNode) {
                // 获得当前文本节点与大小相关的字体属性
                FontAttributesAboutSize fontAttributesAboutSize = getFontAttributesAboutSize(stack, fontName, fontSize);

                // 处理文本节点
                handleTextNode(htmlPreFormatResult, currentChildNode, curLineHtmlTextWidthCounter, curLineHtmlTextMaxFontSizeCounter, fontAttributesAboutSize, allowedMaxWidth);
            } else {
                // 处理非文本节点
                handleNonTextNode(htmlPreFormatResult, curLineHtmlTextWidthCounter, curLineHtmlTextMaxFontSizeCounter, stack, fontName, fontSize, currentChildNode);
            }
        }
        // 解析html，预处理完毕前的操作
        // 尝试设置HTML文本所有行的最大宽度
        htmlPreFormatResult.trySetHtmlTextMaxWidth(curLineHtmlTextWidthCounter.get());

        DebugLogger.log(this.getClass(), "预处理结束前", "记录HTML文本当前行的最大字体大小", curLineHtmlTextMaxFontSizeCounter.get());
        // 记录HTML文本当前行的最大字体大小
        htmlPreFormatResult.recordMaxFontSizePerLine(curLineHtmlTextMaxFontSizeCounter.get());
        return htmlPreFormatResult;
    }

    /**
     * 尝试处理HTML节点的下一个子节点
     *
     * @param currentStackNode
     * @param stack
     * @param htmlPreFormatResult
     * @return
     */
    private boolean tryHandleNextChildNode(Node4HtmlTree currentStackNode, Deque<Node4HtmlTree> stack, HtmlPreFormatResult htmlPreFormatResult) {
        // 增加当前处理的HTML节点的子节点序号
        if (currentStackNode.addChildNodeIndex()) {
            return true;
        }
        // 当前处理的HTML节点的子节点已处理完毕，出栈
        Node4HtmlTree popedNode4HtmlTree = stack.pop();
        Node popedNode = popedNode4HtmlTree.getNode();
        if (popedNode instanceof Element) {
            Element popedElement = (Element) popedNode;
            String tagName = popedElement.tagName();
            if (!"body".equals(tagName)) {
                // 记录HTML元素结束部分，跳过</body>
                StringBuilder content = new StringBuilder("</").append(tagName).append(">");
                HtmlFragment htmlFragment = new HtmlFragment(HtmlFragmentTypeEnum.HFTE_ELEMENT_END, content, false);
                htmlPreFormatResult.addHtmlFragment(htmlFragment);
            }
        }
        return false;
    }

    // 获得当前文本节点与大小相关的字体属性
    private FontAttributesAboutSize getFontAttributesAboutSize(Deque<Node4HtmlTree> stack, String fontName, int fontSize) throws HtmlFormatException {
        FontAttributesAboutSize fontAttributesAboutSize = new FontAttributesAboutSize();
        // 字体与大小相关的属性，默认使用配置参数指定的生命线或消息字体属性
        fontAttributesAboutSize.setFontName(fontName);
        fontAttributesAboutSize.setFontSize(fontSize);

        // 从栈底开始遍历（从body开始），到当前处理的文本节点的父节点为止
        for (Node4HtmlTree node4HtmlTree : stack) {
            Node node = node4HtmlTree.getNode();
            if (!(node instanceof Element)) {
                continue;
            }
            Element element = (Element) node;
            // 处理与字体相关的元素
            handleFontElement(element, fontAttributesAboutSize);
        }
        return fontAttributesAboutSize;
    }

    // 处理与字体相关的元素
    private void handleFontElement(Element element, FontAttributesAboutSize fontAttributesAboutSize) throws HtmlFormatException {
        String tagName = element.tagName();
        if (StringUtils.equalsAny(tagName, "b", "strong")) {
            // 字体加粗
            fontAttributesAboutSize.setBold(true);
            return;
        }
        if (!"font".equals(tagName)) {
            // 当前元素不是font，不处理
            return;
        }
        // 处理font元素
        Attributes attributes = element.attributes();

        // 处理font size
        String size = attributes.get("size");
        if (StringUtils.isNotBlank(size)) {
            throw new HtmlFormatException("指定字体大小时请不要使用 <font size=\"xx\"> ，请使用 <font style=\"font-size: xxpx\"> ");
        }

        // 处理font face
        String face = attributes.get("face");
        if (StringUtils.isNotBlank(face)) {
            fontAttributesAboutSize.setFontName(face);
        }

        String styleString = attributes.get("style");
        if (StringUtils.isBlank(styleString)) {
            return;
        }

        // 处理font style
        handleFontElement(styleString, fontAttributesAboutSize);
    }

    // 处理font style
    private void handleFontElement(String styleString, FontAttributesAboutSize fontAttributesAboutSize) throws HtmlFormatException {
        String[] styleArray = StringUtils.split(styleString, ';');
        for (String style : styleArray) {
            if (StringUtils.isBlank(style)) {
                // 跳过font style中使用;分隔后的空字符串
                continue;
            }
            String[] stylePropertiesArray = StringUtils.split(style, ':');
            if (stylePropertiesArray.length != 2) {
                throw new HtmlFormatException("font style 属性非法，使用 : 分隔后字符串数量不是2 " + style);
            }
            String stylePropertyName = stylePropertiesArray[0].trim();
            String stylePropertyValue = stylePropertiesArray[1].trim();
            switch (stylePropertyName) {
                case "font-size":
                    if (!StringUtils.endsWith(stylePropertyValue, "px")) {
                        throw new HtmlFormatException("通过 font style font-size 属性指定字体大小时，请通过 px 指定字体像素大小");
                    }
                    String fontSize = StringUtils.substringBeforeLast(stylePropertyValue, "px");
                    if (StringUtils.isBlank(fontSize)) {
                        throw new HtmlFormatException("font style font-size 属性指定了 px ，但未指定字体大小数值 " + fontSize);
                    }
                    if (!StringUtils.isNumeric(fontSize)) {
                        throw new HtmlFormatException("font style font-size 属性指定的字体大小数值不是数字 " + fontSize);
                    }
                    int fontSizeValue = Integer.parseInt(fontSize);
                    if (fontSizeValue < USDDIConstants.ALLOWED_MIN_FONT_SIZE) {
                        throw new HtmlFormatException("font style font-size 属性指定的字体大小数值 " + fontSize + " 不能小于允许的最小值 " + USDDIConstants.ALLOWED_MIN_FONT_SIZE);
                    }
                    fontAttributesAboutSize.setFontSize(fontSizeValue);
                    break;
                case "font-family":
                    fontAttributesAboutSize.setFontName(stylePropertyValue);
                    break;
                case "font-weight":
                    if ("bold".equals(stylePropertyValue)) {
                        fontAttributesAboutSize.setBold(true);
                    } else if ("normal".equals(stylePropertyValue)) {
                        fontAttributesAboutSize.setBold(false);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    // 处理文本节点
    private void handleTextNode(HtmlPreFormatResult htmlPreFormatResult, Node currentChildNode, Counter curLineHtmlTextWidthCounter, Counter curLineHtmlTextMaxFontSizeCounter,
                                FontAttributesAboutSize fontAttributesAboutSize, BigDecimal allowedMaxWidth) {
        // 记录html中的文本
        TextNode textNode = (TextNode) currentChildNode;
        String htmlText = textNode.getWholeText().trim();
        if (StringUtils.isBlank(htmlText)) {
            return;
        }

        // 记录当前行文本最大字体大小
        curLineHtmlTextMaxFontSizeCounter.trySetMax(fontAttributesAboutSize.getFontSize());
        // 遍历当前文本的每个字符
        for (int i = 0; i < htmlText.length(); i++) {
            char ch = htmlText.charAt(i);
            // 获得当前字符宽度
            FontMetrics fontMetrics = getFontMetrics(fontAttributesAboutSize);
            int charWidth = fontMetrics.charWidth(ch);
            BigDecimal widthAddChar = BigDecimal.valueOf(curLineHtmlTextWidthCounter.get()).add(BigDecimal.valueOf(charWidth));
            if (widthAddChar.compareTo(allowedMaxWidth) > 0) {
                DebugLogger.log(this.getClass(), "若当前行拼接当前字符，会超过每行允许的最大宽度", "当前行宽度", curLineHtmlTextWidthCounter.get(), "当前字符", ch, "当前字符宽度", charWidth);

                StringBuilder autoAddBrDescription = new StringBuilder();
                if (htmlPreFormatResult.checkAllowAutoAddBr(autoAddBrDescription)) {
                    DebugLogger.log(this.getClass(), "允许自动添加换行");
                    htmlPreFormatResult.addHtmlFragmentBr();

                    DebugLogger.log(this.getClass(), "处理文本节点", "记录HTML文本当前行的最大字体大小", curLineHtmlTextMaxFontSizeCounter.get());
                    htmlPreFormatResult.recordMaxFontSizePerLine(curLineHtmlTextMaxFontSizeCounter.get());
                    // 下一行HTML文本最大字体大小设置为当前字体大小（相当于重置为0后再增加）
                    curLineHtmlTextMaxFontSizeCounter.set(fontAttributesAboutSize.getFontSize());
                } else {
                    DebugLogger.log(this.getClass(), "不允许自动添加换行", autoAddBrDescription);
                }
                // 尝试设置HTML文本所有行的最大宽度
                htmlPreFormatResult.trySetHtmlTextMaxWidth(curLineHtmlTextWidthCounter.get());
                // 下一行HTML文本宽度设置为当前字符宽度（相当于重置为0后再增加）
                curLineHtmlTextWidthCounter.set(charWidth);

                // 在HTML片段中增加HTML文本，内容为当前字符
                HtmlFragment htmlFragmentText = new HtmlFragment(HtmlFragmentTypeEnum.HFTE_TEXT, ch, false);
                htmlPreFormatResult.addHtmlFragment(htmlFragmentText);
                continue;
            }

            // 在当前行添加当前字符
            // 记录是否需要增加HTML片段
            boolean needAddHtmlFragment = true;
            if (!htmlPreFormatResult.isHtmlFragmentListEmpty()) {
                HtmlFragment lastHtmlFragment = htmlPreFormatResult.getLastHtmlFragment();
                if (HtmlFragmentTypeEnum.HFTE_TEXT == lastHtmlFragment.getType()) {
                    // HTML片段列表非空，且最后一个元素为HTML文本时，在后面追加内容
                    lastHtmlFragment.getContent().append(ch);
                    // 记录不需要增加HTML片段
                    needAddHtmlFragment = false;
                }
            }
            if (needAddHtmlFragment) {
                // HTML片段列表为空，或最后一个元素非HTML文本时,在HTML片段列表中添加元素
                HtmlFragment htmlFragment = new HtmlFragment(HtmlFragmentTypeEnum.HFTE_TEXT, ch, false);
                htmlPreFormatResult.addHtmlFragment(htmlFragment);
            }
            int curLineHtmlTextWidthBefore = curLineHtmlTextWidthCounter.get();
            // 当前行的文本宽度增加
            int curLineHtmlTextWidthAfter = curLineHtmlTextWidthCounter.add(charWidth);
            DebugLogger.log(this.getClass(), "在当前行拼接当前字符", "当前行拼接前宽度", curLineHtmlTextWidthBefore, "当前行拼接后宽度", curLineHtmlTextWidthAfter, "当前字符", ch, "当前字符宽度", charWidth);
        }
    }

    // 处理非文本节点
    private void handleNonTextNode(HtmlPreFormatResult htmlPreFormatResult, Counter curLineHtmlTextWidthCounter, Counter curLineHtmlTextMaxFontSizeCounter,
                                   Deque<Node4HtmlTree> stack, String fontName, int fontSize, Node node) throws HtmlFormatException {
        // 执行处理非文本节点
        HtmlFragment htmlFragment = doHandleNonTextNode(htmlPreFormatResult, curLineHtmlTextWidthCounter, curLineHtmlTextMaxFontSizeCounter, stack, fontName, fontSize, node);
        if (htmlFragment != null) {
            // 在HTML片段列表中增加元素
            htmlPreFormatResult.addHtmlFragment(htmlFragment);
        }
    }

    // 执行处理非文本节点
    private HtmlFragment doHandleNonTextNode(HtmlPreFormatResult htmlPreFormatResult, Counter curLineHtmlTextWidthCounter, Counter curLineHtmlTextMaxFontSizeCounter,
                                             Deque<Node4HtmlTree> stack, String fontName, int fontSize, Node node) throws HtmlFormatException {
        if (!(node instanceof Element)) {
            return null;
        }
        // 处理HTML元素
        Element element = (Element) node;
        String tagName = element.tagName();
        // 处理div元素
        if ("div".equals(tagName)) {
            Element parentElement = element.parent();
            if (parentElement == null) {
                throw new HtmlFormatException("<div>的父节点未找到");
            }
            if (!"body".equals(parentElement.tagName())) {
                throw new HtmlFormatException("<div>仅允许在<body>下第一层使用");
            }
            Element firstElement = parentElement.child(0);
            if (!firstElement.equals(element)) {
                throw new HtmlFormatException("<div>仅允许在<body>下第一层作为唯一一个子节点使用");
            }
        }
        // 处理p元素
        if ("p".equals(tagName)) {
            throw new HtmlFormatException("需要人工换行时，请使用<br>代替<p></p>");
        }

        boolean isBrElement = "br".equals(tagName);
        if (isBrElement) {
            // 处理br元素
            handleBrElement(element, htmlPreFormatResult, curLineHtmlTextWidthCounter, curLineHtmlTextMaxFontSizeCounter, stack, fontName, fontSize);
        }

        if (element.childNodeSize() == 0) {
            // 节点不存在子节点时，输出HTML字符内容
            return new HtmlFragment(HtmlFragmentTypeEnum.HFTE_ELEMENT, element.outerHtml(), isBrElement);
        }
        // 节点存在子节点时，入栈
        Node4HtmlTree node4HtmlTree = new Node4HtmlTree(element);
        stack.push(node4HtmlTree);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(tagName);
        // 获得元素用于显示的内容
        String attributeStr = element.attributes().toString();
        if (StringUtils.isNotBlank(attributeStr)) {
            stringBuilder.append(attributeStr);
        }
        stringBuilder.append(">");
        return new HtmlFragment(HtmlFragmentTypeEnum.HFTE_ELEMENT, stringBuilder.toString(), isBrElement);
    }

    // 处理br元素
    private void handleBrElement(Element element, HtmlPreFormatResult htmlPreFormatResult, Counter curLineHtmlTextWidthCounter,
                                 Counter curLineHtmlTextMaxFontSizeCounter, Deque<Node4HtmlTree> stack, String fontName, int fontSize) throws HtmlFormatException {
        // 尝试设置HTML文本所有行的最大宽度
        htmlPreFormatResult.trySetHtmlTextMaxWidth(curLineHtmlTextWidthCounter.get());
        // 下一行HTML文本宽度重置为0
        curLineHtmlTextWidthCounter.setZero();
        DebugLogger.log(this.getClass(), "处理到HTML换行元素，HTML文本宽度重置为0", "指定需要换行的HTML元素", element.tagName());

        // 获得当前文本节点与大小相关的字体属性
        FontAttributesAboutSize fontAttributesAboutSize = getFontAttributesAboutSize(stack, fontName, fontSize);
        // 尝试修改当前行HTML文本最大字体大小
        curLineHtmlTextMaxFontSizeCounter.trySetMax(fontAttributesAboutSize.getFontSize());

        DebugLogger.log(this.getClass(), "处理换行元素", "记录HTML文本当前行的最大字体大小", curLineHtmlTextMaxFontSizeCounter.get());
        // 记录HTML文本当前行的最大字体大小
        htmlPreFormatResult.recordMaxFontSizePerLine(curLineHtmlTextMaxFontSizeCounter.get());
        // 下一行HTML文本最大字体大小重置为0
        curLineHtmlTextMaxFontSizeCounter.setZero();
    }
}
