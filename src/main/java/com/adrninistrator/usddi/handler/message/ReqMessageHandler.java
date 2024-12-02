package com.adrninistrator.usddi.handler.message;

import com.adrninistrator.usddi.conf.ConfPositionInfo;
import com.adrninistrator.usddi.conf.ConfStyleInfo;
import com.adrninistrator.usddi.dto.activation.ActivationInfo;
import com.adrninistrator.usddi.dto.message.MessageInStack;
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
public class ReqMessageHandler extends BaseMessageHandler {

    public ReqMessageHandler(UsedVariables usedVariables, ConfPositionInfo confPositionInfo, ConfStyleInfo confStyleInfo, HtmlHandler htmlHandler) {
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

        // 检查终点Lifeline，不支持循环调用
        for (MessageInStack messageInStack : messageStack) {
            if (messageInText.getEndLifelineSeq().equals(messageInStack.getStartLifelineSeq())) {
                System.err.println("不支持循环调用，消息对应的生命线序号: " + DebugLogger.getLifelineSeq(messageInText.getEndLifelineSeq()));
                return false;
            }
        }

        // 入栈
        MessageInStack currentMessage = new MessageInStack();
        currentMessage.setStartLifelineSeq(messageInText.getStartLifelineSeq());
        currentMessage.setEndLifelineSeq(messageInText.getEndLifelineSeq());
        messageStack.push(currentMessage);

        // 记录Message的坐标
        MessageInfo messageInfo = genMessageInfo(messageInText, usedVariables.getCurrentPartSeq());
        messageInfo.setMessageType(MessageTypeEnum.MTE_REQ);

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
        // 在记录Activation的Map中，终点Lifeline的Activation List中，增加一个Activation，起始y坐标为消息的中y
        Map<Integer, List<ActivationInfo>> activationMap = usedVariables.getActivationMap();
        List<ActivationInfo> endActivationInfoList = activationMap.computeIfAbsent(messageInfo.getEndLifelineSeq(), k -> new ArrayList<>());
        ActivationInfo endActivationInfo = new ActivationInfo();
        endActivationInfoList.add(endActivationInfo);
        endActivationInfo.setTopY(messageInfo.getMiddleY());
        endActivationInfo.setBottomY(null);
        DebugLogger.logActivation(this.getClass(), "addActivation4EndLifeline", messageInfo.getEndLifelineSeq(), endActivationInfo);

        // 尝试为起点Lifeline增加Activation
        tryAddActivation4StartLifeline(messageInfo);
    }
}
