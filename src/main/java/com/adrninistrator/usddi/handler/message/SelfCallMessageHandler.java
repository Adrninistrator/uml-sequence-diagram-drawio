package com.adrninistrator.usddi.handler.message;

import com.adrninistrator.usddi.dto.LifelineInfo;
import com.adrninistrator.usddi.dto.MessageInText;
import com.adrninistrator.usddi.dto.SelfCallMessageInfo;
import com.adrninistrator.usddi.enums.MessageTypeEnum;
import com.adrninistrator.usddi.handler.base.BaseMessageHandler;

import java.util.List;
import java.util.Map;

/**
 * @author adrninistrator
 * @date 2021/9/16
 * @description:
 */
public class SelfCallMessageHandler extends BaseMessageHandler {

    @Override
    public boolean handleMessage(MessageInText messageInText) {
        // 检查栈顶元素
        if (!checkStackTop(messageInText)) {
            return false;
        }

        // 检查Message起点
        if (!checkMessageStart(messageInText)) {
            return false;
        }

        // 处理当前处理到的y坐标，加上Message（及与Lifeline之间）垂直间距
        usedVariables.addCurrentY(confPositionInfo.getMessageVerticalSpacing());

        // 记录Message的坐标
        SelfCallMessageInfo selfCallMessageInfo = new SelfCallMessageInfo();
        selfCallMessageInfo.setMessageType(MessageTypeEnum.MTE_SELF);
        selfCallMessageInfo.setMessageText(messageInText.getMessageText());
        selfCallMessageInfo.setStartLifelineSeq(messageInText.getStartLifelineSeq());
        selfCallMessageInfo.setEndLifelineSeq(messageInText.getEndLifelineSeq());

        // 起点、点1：y坐标为当前处理到的y坐标
        selfCallMessageInfo.setStartY(usedVariables.getCurrentY());
        selfCallMessageInfo.setPoint1Y(usedVariables.getCurrentY());

        Map<String, Integer> lifelineDisplayedNameMap = usedVariables.getLifelineDisplayedNameMap();
        List<LifelineInfo> lifelineInfoList = usedVariables.getLifelineInfoList();
        LifelineInfo startEndLifelineInfo = lifelineInfoList.get(messageInText.getStartLifelineSeq());

        // 起点、终点：x坐标为起点Lifeline中间点的x坐标加上配置中指定的Activation宽度的一半
        selfCallMessageInfo.setStartX(startEndLifelineInfo.getCenterX().add(confPositionInfo.getActivationWidthHalf()));
        selfCallMessageInfo.setEndX(selfCallMessageInfo.getStartX());

        // 点1、点2：x坐标为起点Lifeline中间点的x坐标加上配置中指定的Activation宽度的一半，再加上自调用Message的水平宽度
        selfCallMessageInfo.setPoint1X(selfCallMessageInfo.getStartX().add(confPositionInfo.getSelfCallHorizontalWidth()));
        selfCallMessageInfo.setPoint2X(selfCallMessageInfo.getPoint1X());

        // Activation处理
        handleActivation(messageInText);

        // 处理当前处理到的y坐标，再加上Message请求及返回（自调用）的垂直间距
        usedVariables.addCurrentY(confPositionInfo.getRspMessageVerticalSpacing());

        // 终点、点2：y坐标为当前处理到的y坐标加上Message请求及返回（自调用）的垂直间距
        selfCallMessageInfo.setEndY(usedVariables.getCurrentY());
        selfCallMessageInfo.setPoint2Y(usedVariables.getCurrentY());

        // 记录表Message列表中
        usedVariables.getMessageInfoList().add(selfCallMessageInfo);

        return true;
    }

    // Activation处理
    private void handleActivation(MessageInText messageInText) {
        // 尝试为起点Lifeline增加Activation
        tryAddActivation4StartLifeline(messageInText);
    }
}
