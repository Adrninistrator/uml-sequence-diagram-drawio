package com.adrninistrator.usddi.jaxb.generator;

import com.adrninistrator.usddi.conf.ConfPositionInfo;
import com.adrninistrator.usddi.conf.ConfStyleInfo;
import com.adrninistrator.usddi.dto.*;
import com.adrninistrator.usddi.jaxb.dto.*;
import com.adrninistrator.usddi.jaxb.util.JAXBUtil;
import com.adrninistrator.usddi.util.CommonUtil;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author adrninistrator
 * @date 2021/9/21
 * @description:
 */
public class DrawIoUSDXmlGen {

    public static final String MX_AS_GEOMETRY = "geometry";
    public static final String MX_AS_SOURCE_POINT = "sourcePoint";
    public static final String MX_AS_TARGET_POINT = "targetPoint";
    public static final String MX_AS_POINTS = "points";

    private UsedVariables usedVariables = UsedVariables.getInstance();
    private ConfPositionInfo confPositionInfo = ConfPositionInfo.getInstance();
    private ConfStyleInfo confStyleInfo = ConfStyleInfo.getInstance();

    private List<LifelineInfo> lifelineInfoList = usedVariables.getLifelineInfoList();
    private Map<Integer, List<ActivationInfo>> activationMap = usedVariables.getActivationMap();
    private List<MessageInfo> messageInfoList = usedVariables.getMessageInfoList();

    private String mxCellHead;

    private int mxCellId = 0;

    /**
     * 生成drawio的UML时序图XML文件
     *
     * @param xmlFilePath
     */
    public boolean generate(String xmlFilePath) {
        mxCellHead = "mxCell-" + System.currentTimeMillis() + "-";

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xmlFilePath),
                StandardCharsets.UTF_8))) {
            // 生成基本数据
            MxGraphModel mxGraphModel = genBaseData();
            List<MxCell> mxCellList = mxGraphModel.getRoot().getMxCellList();

            // 处理Lifeline
            handleLifeline(mxCellList);

            // 处理Activation
            handleActivation(mxCellList);

            // 处理Message
            handleMessage(mxCellList);

            // 生成XML文件
            JAXBUtil.javaBeanToXml(mxGraphModel, writer,
                    MxArray.class, MxCell.class, MxGeometry.class, MxGraphModel.class, MxPoint.class, MxRoot.class);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 生成基本数据
    private MxGraphModel genBaseData() {
        MxGraphModel mxGraphModel = new MxGraphModel();
        mxGraphModel.setDx("0");
        mxGraphModel.setDy("0");
        mxGraphModel.setGrid("1");
        mxGraphModel.setGridSize("10");
        mxGraphModel.setGuides("1");
        mxGraphModel.setTooltips("1");
        mxGraphModel.setConnect("1");
        mxGraphModel.setArrows("1");
        mxGraphModel.setFold("1");
        mxGraphModel.setPage("1");
        mxGraphModel.setPageScale("1");
        mxGraphModel.setPageWidth("100");
        mxGraphModel.setPageHeight("100");
        mxGraphModel.setMath("0");
        mxGraphModel.setShadow("0");

        MxRoot root = new MxRoot();
        mxGraphModel.setRoot(root);

        // mxCell数量，等于Lifeline + Activation + Message总数量 + 2
        int cellNum = lifelineInfoList.size();

        Collection<List<ActivationInfo>> activationInfoLists = activationMap.values();
        for (List<ActivationInfo> activationInfoList : activationInfoLists) {
            cellNum += activationInfoList.size();
        }
        cellNum += messageInfoList.size();
        cellNum += 2;

        List<MxCell> mxCellList = new ArrayList<>(cellNum);
        root.setMxCellList(mxCellList);

        MxCell mxCell0 = new MxCell();
        mxCell0.setId("0");

        MxCell mxCell1 = new MxCell();
        mxCell1.setId("1");
        mxCell1.setParent("0");

        mxCellList.add(mxCell0);
        mxCellList.add(mxCell1);

        return mxGraphModel;
    }

    // 处理Lifeline
    private void handleLifeline(List<MxCell> mxCellList) {
        for (LifelineInfo lifelineInfo : lifelineInfoList) {
            MxCell lifelineMxCell = new MxCell();
            lifelineMxCell.setId(genMxId());
            // 处理文字及字体
            String text = handleTextWithFont(lifelineInfo.getDisplayedName(),
                    confStyleInfo.getTextFontOfLifeline(), confStyleInfo.getTextSizeOfLifeline(), confStyleInfo.getTextColorOfLifeline());
            lifelineMxCell.setValue(text);

            // 处理Lifeline样式
            String style = getLifelineStyle();
            lifelineMxCell.setStyle(style);
            lifelineMxCell.setVertex("1");
            lifelineMxCell.setParent("1");

            MxGeometry lifelineMxGeometry = new MxGeometry();
            lifelineMxCell.setMxGeometry(lifelineMxGeometry);
            lifelineMxGeometry.setX(lifelineInfo.getCenterX().subtract(confPositionInfo.getLifelineBoxWidthHalf()).toPlainString());
            lifelineMxGeometry.setY("0");
            lifelineMxGeometry.setWidth(confPositionInfo.getLifelineBoxWidth().toPlainString());
            lifelineMxGeometry.setHeight(usedVariables.getTotalHeight().toPlainString());
            lifelineMxGeometry.setAs(MX_AS_GEOMETRY);

            mxCellList.add(lifelineMxCell);
        }
    }

    // 处理Activation
    private void handleActivation(List<MxCell> mxCellList) {
        int lifelineInfoListSize = lifelineInfoList.size();
        for (int i = 0; i < lifelineInfoListSize; i++) {
            LifelineInfo lifelineInfo = lifelineInfoList.get(i);
            List<ActivationInfo> activationInfoList = activationMap.get(i);
            if (activationInfoList == null) {
                // 当前Lifeline有可能没有Activation
                continue;
            }

            for (ActivationInfo activationInfo : activationInfoList) {
                MxCell activationMxCell = new MxCell();
                activationMxCell.setId(genMxId());
                activationMxCell.setValue("");

                // 处理Activation样式
                String style = getActivationStyle();
                activationMxCell.setStyle(style);
                activationMxCell.setVertex("1");
                activationMxCell.setParent("1");

                MxGeometry activationMxGeometry = new MxGeometry();
                activationMxCell.setMxGeometry(activationMxGeometry);
                activationMxGeometry.setX(lifelineInfo.getCenterX().subtract(confPositionInfo.getActivationWidthHalf()).toPlainString());
                activationMxGeometry.setY(activationInfo.getStartY().toPlainString());
                activationMxGeometry.setWidth(confPositionInfo.getActivationWidth().toPlainString());
                activationMxGeometry.setHeight(activationInfo.getEndY().subtract(activationInfo.getStartY()).toPlainString());
                activationMxGeometry.setAs(MX_AS_GEOMETRY);

                mxCellList.add(activationMxCell);
            }
        }
    }

    // 处理Message
    private void handleMessage(List<MxCell> mxCellList) {
        for (MessageInfo messageInfo : messageInfoList) {
            MxCell messageMxCell = new MxCell();
            messageMxCell.setId(genMxId());
            // 处理文字及字体
            String text = handleTextWithFont(messageInfo.getMessageText(),
                    confStyleInfo.getTextFontOfMessage(), confStyleInfo.getTextSizeOfMessage(), confStyleInfo.getTextColorOfMessage());
            messageMxCell.setValue(text);
            messageMxCell.setParent("1");
            messageMxCell.setEdge("1");

            MxGeometry messageMxGeometry = new MxGeometry();
            messageMxCell.setMxGeometry(messageMxGeometry);
            messageMxGeometry.setRelative("1");
            messageMxGeometry.setAs(MX_AS_GEOMETRY);

            List<MxPoint> mxPointList = new ArrayList<>(2);
            messageMxGeometry.setMxPointList(mxPointList);

            MxPoint sourcePoint = new MxPoint();
            sourcePoint.setX(messageInfo.getStartX().toPlainString());
            sourcePoint.setY(messageInfo.getStartY().toPlainString());
            sourcePoint.setAs(MX_AS_SOURCE_POINT);

            MxPoint targetPoint = new MxPoint();
            targetPoint.setX(messageInfo.getEndX().toPlainString());
            targetPoint.setY(messageInfo.getEndY().toPlainString());
            targetPoint.setAs(MX_AS_TARGET_POINT);

            mxPointList.add(sourcePoint);
            mxPointList.add(targetPoint);

            String style = null;
            switch (messageInfo.getMessageType()) {
                case MTE_REQ:
                    // 处理ReqMessage样式
                    style = getReqMessageStyle();
                    break;
                case MTE_RSP:
                    // 处理RspMessage样式
                    style = getRspMessageStyle();
                    break;
                case MTE_SELF:
                    // 处理SelfMessage样式
                    style = getSelfMessageStyle();

                    SelfCallMessageInfo selfCallMessageInfo = (SelfCallMessageInfo) messageInfo;
                    MxPoint point1 = new MxPoint();
                    point1.setX(selfCallMessageInfo.getPoint1X().toPlainString());
                    point1.setY(selfCallMessageInfo.getPoint1Y().toPlainString());

                    MxPoint point2 = new MxPoint();
                    point2.setX(selfCallMessageInfo.getPoint2X().toPlainString());
                    point2.setY(selfCallMessageInfo.getPoint2Y().toPlainString());

                    List<MxPoint> mxPointList4Self = new ArrayList<>(2);
                    mxPointList4Self.add(point1);
                    mxPointList4Self.add(point2);

                    MxArray array = new MxArray();
                    array.setAs(MX_AS_POINTS);
                    array.setMxPointList(mxPointList4Self);

                    messageMxGeometry.setArray(array);
                    break;
                case MTE_ASYNC:
                    // 处理AsyncMessage样式
                    style = getAsyncMessageStyle();
                    break;
            }
            messageMxCell.setStyle(style);

            mxCellList.add(messageMxCell);
        }
    }

    private String genMxId() {
        return mxCellHead + (++mxCellId);
    }

    // 处理Lifeline样式
    private String getLifelineStyle() {
        Map<String, String> map = new HashMap<>();
        map.put("shape", "umlLifeline");
        map.put("perimeter", "lifelinePerimeter");
        map.put("whiteSpace", "wrap");
        map.put("html", "1");
        map.put("container", "1");
        map.put("collapsible", "0");
        map.put("recursiveResize", "0");
        map.put("outlineConnect", "0");
        map.put("size", confPositionInfo.getLifelineBoxHeight().toPlainString());
        if (confStyleInfo.getLineWidthOfLifeline() != null) {
            map.put("strokeWidth", confStyleInfo.getLineWidthOfLifeline().toPlainString());
        }
        if (!CommonUtil.isStrEmpty(confStyleInfo.getLineColorOfLifeline())) {
            map.put("strokeColor", confStyleInfo.getLineColorOfLifeline());
        }
        if (!CommonUtil.isStrEmpty(confStyleInfo.getBoxColorOfLifeline())) {
            map.put("fillColor", confStyleInfo.getBoxColorOfLifeline());
        }

        return getStyleStringFromMap(map);
    }

    // 处理Activation样式
    private String getActivationStyle() {
        Map<String, String> map = new HashMap<>();
        map.put("html", "1");
        map.put("points", "[]");
        map.put("perimeter", "orthogonalPerimeter");
        if (confStyleInfo.getLineWidthOfActivation() != null) {
            map.put("strokeWidth", confStyleInfo.getLineWidthOfActivation().toPlainString());
        }
        if (!CommonUtil.isStrEmpty(confStyleInfo.getLineColorOfActivation())) {
            map.put("strokeColor", confStyleInfo.getLineColorOfActivation());
        }
        if (!CommonUtil.isStrEmpty(confStyleInfo.getBoxColorOfActivation())) {
            map.put("fillColor", confStyleInfo.getBoxColorOfActivation());
        }

        return getStyleStringFromMap(map);
    }

    // 处理Message样式
    private Map<String, String> getMessageStyle() {
        Map<String, String> map = new HashMap<>();
        if (confStyleInfo.getLineWidthOfMessage() != null) {
            map.put("strokeWidth", confStyleInfo.getLineWidthOfMessage().toPlainString());
        }
        if (!CommonUtil.isStrEmpty(confStyleInfo.getLineColorOfMessage())) {
            map.put("strokeColor", confStyleInfo.getLineColorOfMessage());
        }
        return map;
    }

    // 处理ReqMessage样式
    private String getReqMessageStyle() {
        Map<String, String> map = getMessageStyle();
        map.put("endArrow", "block");
        map.put("html", "1");
        map.put("endFill", "1");

        return getStyleStringFromMap(map);
    }

    // 处理RspMessage样式
    private String getRspMessageStyle() {
        Map<String, String> map = getMessageStyle();
        map.put("endArrow", "open");
        map.put("html", "1");
        map.put("dashed", "1");
        map.put("endFill", "0");

        return getStyleStringFromMap(map);
    }

    // 处理SelfMessage样式
    private String getSelfMessageStyle() {
        Map<String, String> map = getMessageStyle();
        map.put("endArrow", "block");
        map.put("html", "1");
        map.put("rounded", "0");
        map.put("endFill", "1");
        map.put("align", "left");

        return getStyleStringFromMap(map);
    }

    // 处理AsyncMessage样式
    private String getAsyncMessageStyle() {
        Map<String, String> map = getMessageStyle();
        map.put("endArrow", "open");
        map.put("html", "1");
        map.put("endFill", "0");

        return getStyleStringFromMap(map);
    }

    private String getStyleStringFromMap(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            stringBuilder.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append(";");
        }
        return stringBuilder.toString();
    }

    // 处理文字及字体
    private String handleTextWithFont(String text, String textFont, Integer textSize, String textColor) {
        if (text.contains("<font") && text.contains("</font>")) {
            // 若当前文本中有指定字体，则不使用统一配置的字体
            return text;
        }

        StringBuilder textStyle = new StringBuilder();
        if (!CommonUtil.isStrEmpty(textFont)) {
            textStyle.append(" ").append("face=\"").append(textFont).append("\"");
        }
        if (textSize != null) {
            textStyle.append(" ").append("style=\"font-size: ").append(textSize).append("px\"");
        }
        if (!CommonUtil.isStrEmpty(textColor)) {
            textStyle.append(" ").append("color=\"").append(textColor).append("\"");
        }
        if (textStyle.length() == 0) {
            return text;
        }

        return "<font" + textStyle + ">" + text + "</font>";
    }
}
