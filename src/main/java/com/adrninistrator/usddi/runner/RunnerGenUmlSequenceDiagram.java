package com.adrninistrator.usddi.runner;

import com.adrninistrator.usddi.common.USDDIConstants;
import com.adrninistrator.usddi.conf.ConfManager;
import com.adrninistrator.usddi.conf.ConfPositionInfo;
import com.adrninistrator.usddi.conf.ConfStyleInfo;
import com.adrninistrator.usddi.dto.common.Counter;
import com.adrninistrator.usddi.dto.html.HtmlFormatResult;
import com.adrninistrator.usddi.dto.lifeline.LifelineName;
import com.adrninistrator.usddi.dto.message.MessageInText;
import com.adrninistrator.usddi.dto.variables.UsedVariables;
import com.adrninistrator.usddi.enums.MessageTypeEnum;
import com.adrninistrator.usddi.exceptions.HtmlFormatException;
import com.adrninistrator.usddi.handler.DescriptionHandler;
import com.adrninistrator.usddi.handler.EndAllHandler;
import com.adrninistrator.usddi.handler.EndPartHandler;
import com.adrninistrator.usddi.handler.LifelineHandler;
import com.adrninistrator.usddi.handler.message.AsyncMessageHandler;
import com.adrninistrator.usddi.handler.message.ReqMessageHandler;
import com.adrninistrator.usddi.handler.message.RspMessageHandler;
import com.adrninistrator.usddi.handler.message.SelfCallMessageHandler;
import com.adrninistrator.usddi.html.HtmlHandler;
import com.adrninistrator.usddi.jaxb.generator.DrawIoUSDXmlGen;
import com.adrninistrator.usddi.logger.DebugLogger;
import com.adrninistrator.usddi.util.USDDIUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author adrninistrator
 * @date 2021/9/15
 * @description: 生成draw.io格式的UML时序图入口类
 */
public class RunnerGenUmlSequenceDiagram {

    private final AtomicBoolean running = new AtomicBoolean(false);

    private UsedVariables usedVariables = null;

    private ConfStyleInfo confStyleInfo = null;

    private HtmlHandler htmlHandler = null;

    private AsyncMessageHandler asyncMessageHandler = null;
    private ReqMessageHandler reqMessageHandler = null;
    private RspMessageHandler rspMessageHandler = null;
    private SelfCallMessageHandler selfCallMessageHandler = null;

    private DescriptionHandler descriptionHandler = null;
    private LifelineHandler lifelineHandler = null;
    private EndAllHandler endAllHandler = null;
    private EndPartHandler endPartHandler = null;

    private DrawIoUSDXmlGen drawIoUSDXmlGen = null;

    // Lifeline方框最大允许的宽度
    private BigDecimal lifelineBoxMaxAllowedWidth = null;

    // 记录是否已处理过描述
    private boolean handleDescriptionDone = false;

    // 记录是否已处理完毕前面的lifeline的name
    private boolean handleLifelineNameDone = false;

    public static void main(String[] args) {
        int argNum = 0;
        if (args != null) {
            argNum = args.length;
        }
        if (argNum != 1) {
            System.err.println("请在参数中指定需要生成UML时序图的文本文件，支持指定一个文件，当前参数数量: " + argNum);
            return;
        }

        new RunnerGenUmlSequenceDiagram().generate(args[0]);
    }

    // 初始化
    private boolean init() {
        usedVariables = new UsedVariables();

        ConfManager confManager = new ConfManager();
        if (!confManager.handlePositionConf() || !confManager.handleStyleConf()) {
            return false;
        }

        ConfPositionInfo confPositionInfo = confManager.getConfPositionInfo();
        confStyleInfo = confManager.getConfStyleInfo();

        htmlHandler = new HtmlHandler();

        asyncMessageHandler = new AsyncMessageHandler(usedVariables, confPositionInfo, confStyleInfo, htmlHandler);
        reqMessageHandler = new ReqMessageHandler(usedVariables, confPositionInfo, confStyleInfo, htmlHandler);
        rspMessageHandler = new RspMessageHandler(usedVariables, confPositionInfo, confStyleInfo, htmlHandler);
        selfCallMessageHandler = new SelfCallMessageHandler(usedVariables, confPositionInfo, confStyleInfo, htmlHandler);

        descriptionHandler = new DescriptionHandler(usedVariables, confPositionInfo, confStyleInfo, htmlHandler);
        lifelineHandler = new LifelineHandler(usedVariables, confPositionInfo, confStyleInfo, htmlHandler);
        endAllHandler = new EndAllHandler(usedVariables, confPositionInfo, confStyleInfo, htmlHandler);
        endPartHandler = new EndPartHandler(usedVariables, confPositionInfo, confStyleInfo, htmlHandler);

        drawIoUSDXmlGen = new DrawIoUSDXmlGen(usedVariables, confPositionInfo, confStyleInfo);

        // Lifeline方框最大允许的宽度，生命线中间点的水平间距的65%
        lifelineBoxMaxAllowedWidth = confPositionInfo.getLifelineCenterHorizontalSpacing().multiply(USDDIConstants.LIFELINE_BOX_MAX_WIDTH_PERCENTAGE_LIFELINE);

        handleDescriptionDone = false;
        handleLifelineNameDone = false;
        return true;
    }

    /**
     * 根据文本生成UML时序图文件，使用固定的输出文件名格式
     * 当前方法不允许并发执行
     *
     * @param txtFilePath 输入文本文件路径
     * @return
     */
    public boolean generate(String txtFilePath) {
        String outputFilePath = txtFilePath + "-" + USDDIUtil.currentTime() + USDDIConstants.EXT_DRAWIO;
        return generate(txtFilePath, outputFilePath);
    }

    /**
     * 根据文本生成UML时序图文件，指定输出文件名
     * 当前方法不允许并发执行
     *
     * @param txtFilePath    输入文本文件路径
     * @param outputFilePath 生成的UML时序图文件路径
     * @return
     */
    public boolean generate(String txtFilePath, String outputFilePath) {
        try {
            if (!running.compareAndSet(false, true)) {
                System.err.println(this.getClass().getSimpleName() + " 当前类不允许并发执行，假如需要并发执行请创建新的实例");
            }

            // 初始化
            if (!init()) {
                return false;
            }

            boolean success = doGenerate(txtFilePath, outputFilePath);
            DebugLogger.beforeExit();
            return success;
        } finally {
            running.set(false);
        }
    }

    public boolean doGenerate(String txtFilePath, String outputFilePath) {
        File txtFile = new File(txtFilePath);
        if (!txtFile.exists()) {
            System.err.println("指定的文件不存在: " + txtFilePath);
            return false;
        }
        if (!txtFile.isFile()) {
            System.err.println("指定的不是文件: " + txtFilePath);
            return false;
        }

        int lineNum = 0;
        // Lifeline方框的最大宽度与高度
        Counter lifelineBoxMaxWidth = new Counter();
        Counter lifelineBoxMaxHeight = new Counter();
        // 标记上一行是否为空行
        boolean lastLineIsEmpty = false;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(txtFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineNum++;

                if (lineNum == 1 && line.startsWith("\uFEFF")) {
                    System.err.println("当前文件编码为UTF-8-BOM，请修改为UTF-8无BOM");
                    return false;
                }

                if (line.startsWith(USDDIConstants.COMMENT_FLAG)) {
                    continue;
                }

                if (!lastLineIsEmpty && line.trim().isEmpty()) {
                    // 若上一行非空行，且当前行为空行，代表部分结束
                    if (!endPartHandler.handle()) {
                        System.err.println("开始处理新的部分，第" + lineNum + "行处理失败: " + line);
                        return false;
                    }
                    // 标记上一行为空行
                    lastLineIsEmpty = true;
                    continue;
                }

                // 标记上一行非空行
                lastLineIsEmpty = false;

                if (line.startsWith(USDDIConstants.DESCRIPTION_FLAG)) {
                    // 当前行为描述
                    if (!handleDescription(line, lineNum)) {
                        return false;
                    }
                } else if (line.startsWith(USDDIConstants.LIFELINE_TITLE_FLAG)) {
                    // 当前行为lifeline的name
                    if (!handleLifeLine(line, lineNum, lifelineBoxMaxWidth, lifelineBoxMaxHeight)) {
                        return false;
                    }
                } else {
                    // 当前行为Message
                    if (!handleLifelineNameDone) {
                        // 第一次处理消息，记录lifeline的name已处理完毕
                        handleLifelineNameDone = true;
                        // 在处理完所有的Lifeline之后进行处理
                        handleLifeLineAfter(lifelineBoxMaxWidth, lifelineBoxMaxHeight);
                    }

                    // 对Message进行处理
                    if (!handleMessage(line, lineNum)) {
                        return false;
                    }
                }
            }

            // 循环结束
            // 若上一行为非空行，执行部分结束的操作
            if (!lastLineIsEmpty && !endPartHandler.handle()) {
                return false;
            }

            // 全部结束
            if (!endAllHandler.handle()) {
                return false;
            }

            // 生成drawio的XML文件
            if (!drawIoUSDXmlGen.generate(outputFilePath)) {
                return false;
            }

            System.out.println("生成UML时序图处理完毕 " + outputFilePath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 处理描述信息
    private boolean handleDescription(String line, int lineNum) {
        if (handleDescriptionDone) {
            System.err.println("只允许在生命线及消息前指定一个描述，第" + lineNum + "行还是描述: " + line);
            return false;
        }

        // 处理描述
        descriptionHandler.handleDescription(line);

        handleDescriptionDone = true;
        return true;
    }

    // 处理Lifeline
    private boolean handleLifeLine(String line, int lineNum, Counter lifelineBoxMaxWidth, Counter lifelineBoxMaxHeight) throws HtmlFormatException {
        if (handleLifelineNameDone) {
            System.err.println("生命线名称已处理完毕，第" + lineNum + "行还是生命线名称: " + line);
            return false;
        }

        // 添加Lifeline
        LifelineName lifelineName = lifelineHandler.addLifeline(line);
        if (lifelineName == null) {
            System.err.println("添加第" + lineNum + "行生命线名称失败: " + line);
            return false;
        }

        // 处理Lifeline方框中的文本，获得方框最大宽度与高度
        HtmlFormatResult htmlFormatResult = htmlHandler.formatHtml(lifelineName.getDisplayedName(), lifelineBoxMaxAllowedWidth,
                confStyleInfo.getTextFontOfLifeline(), confStyleInfo.getTextSizeOfLifeline());
        if (htmlFormatResult.getWidth() > lifelineBoxMaxWidth.get()) {
            lifelineBoxMaxWidth.set(htmlFormatResult.getWidth());
        }
        if (htmlFormatResult.getHeight() > lifelineBoxMaxHeight.get()) {
            lifelineBoxMaxHeight.set(htmlFormatResult.getHeight());
        }

        return true;
    }

    // 在处理完所有的Lifeline之后进行处理
    private void handleLifeLineAfter(Counter lifelineBoxMaxWidth, Counter lifelineBoxMaxHeight) {
        // 记录Lifeline方框实际使用的高度，使用文字实际高度的2倍，与文字实际高度加30，取最小值
        BigDecimal lifelineBoxHeight = USDDIUtil.minBigDecimal(
                BigDecimal.valueOf(lifelineBoxMaxHeight.get()).multiply(USDDIConstants.LIFELINE_BOX_MIN_HEIGHT_MULTIPLE_TEXT),
                BigDecimal.valueOf(lifelineBoxMaxHeight.get()).add(USDDIConstants.LIFELINE_BOX_MIN_HEIGHT_ADD));
        usedVariables.setLifelineBoxActualHeight(lifelineBoxHeight);

        // 记录Lifeline方框实际使用的宽度
        BigDecimal lifelineBoxWidth;
        BigDecimal lifelineBoxMaxWidthValue = BigDecimal.valueOf(lifelineBoxMaxWidth.get());
        BigDecimal lifelineBoxHeightMultiple = lifelineBoxHeight.multiply(USDDIConstants.LIFELINE_BOX_WIDTH_HEIGHT_MULTIPLE);
        if (lifelineBoxMaxWidthValue.compareTo(lifelineBoxHeightMultiple) > 0) {
            // 若Lifeline方框的最大宽度大于实际高度的2倍，则使用最大宽度
            lifelineBoxWidth = lifelineBoxMaxWidthValue;
        } else {
            // 若Lifeline方框的最大宽度小于等于实际高度的2倍，则使用实际高度的2倍与最大允许宽度，取最小值
            lifelineBoxWidth = USDDIUtil.minBigDecimal(lifelineBoxHeightMultiple, lifelineBoxMaxAllowedWidth);
        }

        // Lifeline方框使用的宽度需要再加一些，加2可能不够，使用加4
        usedVariables.setLifelineBoxActualWidth(lifelineBoxWidth.add(USDDIConstants.LIFELINE_BOX_BORDER_TEXT_SPACE_HORIZONTAL));
        usedVariables.setLifelineBoxActualWidthHalf(USDDIUtil.getHalfBigDecimal(usedVariables.getLifelineBoxActualWidth()));

        DebugLogger.log(this.getClass(), "Lifeline方框 最大宽度", usedVariables.getLifelineBoxActualWidth(), "最大高度", usedVariables.getLifelineBoxActualHeight());

        // 设置Lifeline的中间点x坐标
        lifelineHandler.setLifelineCenterX();

        // 当前处理到的y坐标值加上Lifeline方框高度
        usedVariables.addCurrentY(this.getClass(), "加上Lifeline方框高度", usedVariables.getLifelineBoxActualHeight());
    }

    /**
     * 对Message进行处理
     *
     * @param line
     * @param lineNum
     * @return
     */
    private boolean handleMessage(String line, int lineNum) throws HtmlFormatException {
        if (usedVariables.getLifelineInfoList().isEmpty()) {
            System.err.println("第" + lineNum + "行未指定生命线的名称: " + line);
            return false;
        }

        // 获得Message中的标志
        MessageInText messageInText = USDDIUtil.genMessageInText(line, usedVariables);
        if (messageInText == null) {
            System.err.println("第" + lineNum + "行生命线名称处理失败: " + line);
            return false;
        }
        MessageTypeEnum messageType = messageInText.getMessageType();
        boolean success = false;
        switch (messageType) {
            case MTE_REQ:
                success = reqMessageHandler.handleMessage(messageInText);
                break;
            case MTE_RSP:
                success = rspMessageHandler.handleMessage(messageInText);
                break;
            case MTE_SELF:
                success = selfCallMessageHandler.handleMessage(messageInText);
                break;
            case MTE_ASYNC:
                success = asyncMessageHandler.handleMessage(messageInText);
                break;
        }
        if (!success) {
            System.err.println("第" + lineNum + "行消息处理失败: " + line);
            return false;
        }
        return true;
    }
}
