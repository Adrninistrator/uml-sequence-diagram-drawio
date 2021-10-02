package com.adrninistrator.usddi.handler.message;

import com.adrninistrator.usddi.common.Constants;
import com.adrninistrator.usddi.dto.MessageInStack;
import com.adrninistrator.usddi.dto.MessageInText;
import com.adrninistrator.usddi.dto.MessageInfo;
import com.adrninistrator.usddi.enums.MessageTypeEnum;
import com.adrninistrator.usddi.handler.base.BaseMessageHandler;

import java.util.List;

/**
 * @author adrninistrator
 * @date 2021/9/16
 * @description:
 */
public class RspMessageHandler extends BaseMessageHandler {

    @Override
    public boolean handleMessage(MessageInText messageInText) {
        if (messageStack.isEmpty()) {
            System.err.println("当前返回Message不存在对应的请求Message");
            return false;
        }

        // 出栈，获取对应的请求Message
        MessageInStack messageInStack = messageStack.pop();
        // 检查出栈记录与当前的Message返回起点终点对应
        if (!messageInText.getStartLifelineSeq().equals(messageInStack.getEndLifelineSeq()) ||
                !messageInText.getEndLifelineSeq().equals(messageInStack.getStartLifelineSeq())) {
            System.err.println("当前返回Message与上一条请求Message不匹配: " + messageInStack.getStartLifelineSeq() +
                    Constants.MESSAGE_REQ_FLAG + messageInStack.getEndLifelineSeq());
            return false;
        }

        // 处理当前处理到的y坐标
        List<MessageInfo> messageInfoList = usedVariables.getMessageInfoList();
        MessageInfo lastMessageInfo = messageInfoList.get(messageInfoList.size() - 1);
        if (lastMessageInfo.getStartLifelineSeq().equals(messageInText.getEndLifelineSeq()) &&
                lastMessageInfo.getEndLifelineSeq().equals(messageInText.getStartLifelineSeq())) {
            // 记录Message的List的最后一个Message，与刚出栈的请求Message是同一个
            // 处理当前处理到的y坐标加上Message请求及返回（自调用）的垂直间距
            usedVariables.addCurrentY(confPositionInfo.getRspMessageVerticalSpacing());
        } else {
            // 记录Message的List的最后一个Message，与刚出栈的请求Message不是同一个
            // 加上Message（及与Lifeline之间）垂直间距
            usedVariables.addCurrentY(confPositionInfo.getMessageVerticalSpacing());
        }

        // 记录Message的坐标
        MessageInfo messageInfo = MessageInfo.genFromMessageInText(messageInText);
        messageInfo.setMessageType(MessageTypeEnum.MTE_RSP);

        // 处理Message位置
        setMessagePosition(messageInfo, messageInText);

        // 记录表Message列表中
        usedVariables.getMessageInfoList().add(messageInfo);

        // Activation处理
        return handleActivation(messageInText);
    }

    // Activation处理
    private boolean handleActivation(MessageInText messageInText) {
        // 为当前Message的起点对应的Lifeline的Activation设置结束y坐标
        return setActivationEndY(messageInText.getStartLifelineSeq());
    }
}
