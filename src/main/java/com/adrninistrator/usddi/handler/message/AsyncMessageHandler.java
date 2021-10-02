package com.adrninistrator.usddi.handler.message;

import com.adrninistrator.usddi.dto.ActivationInfo;
import com.adrninistrator.usddi.dto.MessageInText;
import com.adrninistrator.usddi.dto.MessageInfo;
import com.adrninistrator.usddi.enums.MessageTypeEnum;
import com.adrninistrator.usddi.handler.base.BaseMessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author adrninistrator
 * @date 2021/9/16
 * @description:
 */
public class AsyncMessageHandler extends BaseMessageHandler {

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
        MessageInfo messageInfo = MessageInfo.genFromMessageInText(messageInText);
        messageInfo.setMessageType(MessageTypeEnum.MTE_ASYNC);

        // 处理Message位置
        setMessagePosition(messageInfo, messageInText);

        // 记录表Message列表中
        usedVariables.getMessageInfoList().add(messageInfo);

        // Activation处理
        handleActivation(messageInText);

        return true;
    }

    // Activation处理
    private void handleActivation(MessageInText messageInText) {
        // 在记录Activation的Map中，终点Lifeline的Activation List中，增加一个Activation
        // 起始y坐标为当前处理到的y坐标，结束y坐标为当前处理到的y坐标加上Message（及与Lifeline之间）垂直间距的一半
        Map<Integer, List<ActivationInfo>> activationMap = usedVariables.getActivationMap();
        List<ActivationInfo> endActivationInfoList = activationMap.computeIfAbsent(messageInText.getEndLifelineSeq(), k -> new ArrayList<>());
        ActivationInfo endActivationInfo = new ActivationInfo();
        endActivationInfoList.add(endActivationInfo);
        endActivationInfo.setStartY(usedVariables.getCurrentY());
        endActivationInfo.setEndY(usedVariables.getCurrentY().add(confPositionInfo.getMessageVerticalSpacingHalf()));

        // 尝试为起点Lifeline增加Activation
        tryAddActivation4StartLifeline(messageInText);
    }
}
