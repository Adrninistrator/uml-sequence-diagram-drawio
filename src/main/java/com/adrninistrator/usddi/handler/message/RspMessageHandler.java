package com.adrninistrator.usddi.handler.message;

import com.adrninistrator.usddi.common.USDDIConstants;
import com.adrninistrator.usddi.conf.ConfPositionInfo;
import com.adrninistrator.usddi.conf.ConfStyleInfo;
import com.adrninistrator.usddi.dto.message.MessageInStack;
import com.adrninistrator.usddi.dto.message.MessageInText;
import com.adrninistrator.usddi.dto.message.MessageInfo;
import com.adrninistrator.usddi.dto.variables.UsedVariables;
import com.adrninistrator.usddi.enums.MessageTypeEnum;
import com.adrninistrator.usddi.exceptions.HtmlFormatException;
import com.adrninistrator.usddi.handler.base.BaseMessageHandler;
import com.adrninistrator.usddi.html.HtmlHandler;
import com.adrninistrator.usddi.logger.DebugLogger;

/**
 * @author adrninistrator
 * @date 2021/9/16
 * @description:
 */
public class RspMessageHandler extends BaseMessageHandler {

    public RspMessageHandler(UsedVariables usedVariables, ConfPositionInfo confPositionInfo, ConfStyleInfo confStyleInfo, HtmlHandler htmlHandler) {
        super(usedVariables, confPositionInfo, confStyleInfo, htmlHandler);
    }

    @Override
    public boolean handleMessage(MessageInText messageInText) throws HtmlFormatException {
        DebugLogger.logMessageInText(this.getClass(), "handleMessage", messageInText);

        if (messageStack.isEmpty()) {
            System.err.println("当前返回消息不存在对应的请求消息，生命线序号: " +
                    DebugLogger.getLifelineSeq(messageInText.getStartLifelineSeq()) +
                    USDDIConstants.MESSAGE_REQ_FLAG +
                    DebugLogger.getLifelineSeq(messageInText.getEndLifelineSeq()));
            return false;
        }

        // 出栈，获取对应的请求Message
        MessageInStack messageInStack = messageStack.pop();
        // 检查出栈记录与当前的Message返回起点终点对应
        if (!messageInText.getStartLifelineSeq().equals(messageInStack.getEndLifelineSeq()) ||
                !messageInText.getEndLifelineSeq().equals(messageInStack.getStartLifelineSeq())) {
            System.err.println("当前返回消息与上一条请求消息不匹配: " + messageInStack.getStartLifelineSeq() +
                    USDDIConstants.MESSAGE_REQ_FLAG + messageInStack.getEndLifelineSeq());
            return false;
        }

        // 记录Message的坐标
        MessageInfo messageInfo = genMessageInfo(messageInText, usedVariables.getCurrentPartSeq());
        messageInfo.setMessageType(MessageTypeEnum.MTE_RSP);

        // 处理Message位置
        handleMessagePosition(messageInfo);

        // 记录到Message列表中
        messageInfoList.add(messageInfo);

        // Activation处理
        return handleActivation(messageInfo);
    }

    // Activation处理
    private boolean handleActivation(MessageInfo messageInfo) {
        // 为当前Message的起点对应的Lifeline的Activation设置结束y坐标
        // 返回消息，激活下y使用消息的中y
        return setActivationEndY(messageInfo.getMiddleY(), messageInfo.getStartLifelineSeq());
    }
}
