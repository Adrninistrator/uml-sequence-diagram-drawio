package com.adrninistrator.usddi.handler.message;

import com.adrninistrator.usddi.conf.ConfPositionInfo;
import com.adrninistrator.usddi.conf.ConfStyleInfo;
import com.adrninistrator.usddi.dto.activation.ActivationInfo;
import com.adrninistrator.usddi.dto.message.MessageInText;
import com.adrninistrator.usddi.dto.message.MessageInfo;
import com.adrninistrator.usddi.dto.variables.UsedVariables;
import com.adrninistrator.usddi.enums.MessageTypeEnum;
import com.adrninistrator.usddi.exceptions.HtmlFormatException;
import com.adrninistrator.usddi.handler.base.BaseMessageHandler;
import com.adrninistrator.usddi.html.HtmlHandler;
import com.adrninistrator.usddi.logger.DebugLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author adrninistrator
 * @date 2021/9/16
 * @description:
 */
public class AsyncMessageHandler extends BaseMessageHandler {

    public AsyncMessageHandler(UsedVariables usedVariables, ConfPositionInfo confPositionInfo, ConfStyleInfo confStyleInfo, HtmlHandler htmlHandler) {
        super(usedVariables, confPositionInfo, confStyleInfo, htmlHandler);
    }

    @Override
    public boolean handleMessage(MessageInText messageInText) throws HtmlFormatException {
        DebugLogger.logMessageInText(this.getClass(), "handleMessage", messageInText);

        // 检查栈顶元素
        if (!checkStackTop(messageInText)) {
            return false;
        }

        // 检查Message起点
        if (!checkMessageStart(messageInText)) {
            return false;
        }

        // 记录Message的坐标
        MessageInfo messageInfo = genMessageInfo(messageInText, usedVariables.getCurrentPartSeq());
        messageInfo.setMessageType(MessageTypeEnum.MTE_ASYNC);

        // 处理Message位置
        handleMessagePosition(messageInfo);

        // 记录到Message列表中
        messageInfoList.add(messageInfo);

        // Activation处理
        handleActivation(messageInfo);

        return true;
    }

    // Activation处理
    private void handleActivation(MessageInfo messageInfo) {
        // 在记录Activation的Map中，终点Lifeline的Activation List中，增加一个Activation
        // 起始y坐标为消息的中y，结束y坐标为消息的中y加上Message（及与Lifeline之间）垂直间距
        Map<Integer, List<ActivationInfo>> activationMap = usedVariables.getActivationMap();
        List<ActivationInfo> endActivationInfoList = activationMap.computeIfAbsent(messageInfo.getEndLifelineSeq(), k -> new ArrayList<>());
        ActivationInfo endActivationInfo = new ActivationInfo();
        endActivationInfoList.add(endActivationInfo);
        endActivationInfo.setTopY(messageInfo.getMiddleY());
        endActivationInfo.setBottomY(messageInfo.getMiddleY().add(confPositionInfo.getMessageVerticalSpacing()));
        DebugLogger.logActivation(this.getClass(), "addActivation4EndLifeline", messageInfo.getEndLifelineSeq(), endActivationInfo);

        // 处理异步消息终点对应激活的下y
        messageInfo.setAsyncMessageEndActivationBottomY(endActivationInfo.getBottomY());

        // 尝试为起点Lifeline增加Activation
        tryAddActivation4StartLifeline(messageInfo);
    }
}
