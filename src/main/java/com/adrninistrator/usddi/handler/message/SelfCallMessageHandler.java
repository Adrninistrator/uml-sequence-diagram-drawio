package com.adrninistrator.usddi.handler.message;

import com.adrninistrator.usddi.conf.ConfPositionInfo;
import com.adrninistrator.usddi.conf.ConfStyleInfo;
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
public class SelfCallMessageHandler extends BaseMessageHandler {

    public SelfCallMessageHandler(UsedVariables usedVariables, ConfPositionInfo confPositionInfo, ConfStyleInfo confStyleInfo, HtmlHandler htmlHandler) {
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
        messageInfo.setMessageType(MessageTypeEnum.MTE_SELF);

        /*
            自调用消息高度处理，如果只有一行文字时会显得太矮
            因此当高度比消息（及与生命线之间）垂直间距小时，将高度设置为消息（及与生命线之间）垂直间距
         */
        if (messageInfo.getHeight().compareTo(confPositionInfo.getMessageVerticalSpacing()) < 0) {
            messageInfo.setHeight(confPositionInfo.getMessageVerticalSpacing());
        }

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
        // 尝试为起点Lifeline增加Activation
        tryAddActivation4StartLifeline(messageInfo);
    }
}
