package com.adrninistrator.usddi.handler.base;

import com.adrninistrator.usddi.common.USDDIConstants;
import com.adrninistrator.usddi.conf.ConfPositionInfo;
import com.adrninistrator.usddi.conf.ConfStyleInfo;
import com.adrninistrator.usddi.dto.activation.ActivationInfo;
import com.adrninistrator.usddi.dto.html.HtmlFormatResult;
import com.adrninistrator.usddi.dto.message.MessageInStack;
import com.adrninistrator.usddi.dto.message.MessageInText;
import com.adrninistrator.usddi.dto.message.MessageInfo;
import com.adrninistrator.usddi.dto.variables.UsedVariables;
import com.adrninistrator.usddi.exceptions.HtmlFormatException;
import com.adrninistrator.usddi.html.HtmlHandler;
import com.adrninistrator.usddi.logger.DebugLogger;
import com.adrninistrator.usddi.util.USDDIUtil;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * @author adrninistrator
 * @date 2021/9/16
 * @description:
 */
public abstract class BaseHandler {

    protected final UsedVariables usedVariables;
    protected final ConfPositionInfo confPositionInfo;
    protected final ConfStyleInfo confStyleInfo;
    protected final HtmlHandler htmlHandler;
    protected final List<MessageInfo> messageInfoList;
    protected final Deque<MessageInStack> messageStack;

    protected BaseHandler(UsedVariables usedVariables, ConfPositionInfo confPositionInfo, ConfStyleInfo confStyleInfo, HtmlHandler htmlHandler) {
        this.usedVariables = usedVariables;
        this.confPositionInfo = confPositionInfo;
        this.confStyleInfo = confStyleInfo;
        this.htmlHandler = htmlHandler;
        messageInfoList = usedVariables.getMessageInfoList();
        messageStack = usedVariables.getMessageStack();
    }

    /**
     * 为当前Message的起点或终点对应的Lifeline的Activation设置结束y坐标
     *
     * @param activationBottomY     激活的下y
     * @param activationLifelineSeq 激活对应的生命线序号
     * @return
     */
    protected boolean setActivationEndY(BigDecimal activationBottomY, Integer activationLifelineSeq) {

        // 在记录Activation的Map中，起/终点Lifeline的Activation List中，判断最后一个Activation
        Map<Integer, List<ActivationInfo>> activationMap = usedVariables.getActivationMap();
        List<ActivationInfo> activationInfoList = activationMap.get(activationLifelineSeq);
        // 最后一个Activation应满足存在，且结束y坐标未设置
        if (activationInfoList == null) {
            System.err.println("未找到对应的激活，对应生命线序号: " + DebugLogger.getLifelineSeq(activationLifelineSeq));
            return false;
        }
        ActivationInfo lastActivationInfo = activationInfoList.get(activationInfoList.size() - 1);
        if (lastActivationInfo.getBottomY() != null) {
            System.err.println("最后一个激活的结束y坐标已设置，对应生命线序号: " + DebugLogger.getLifelineSeq(activationLifelineSeq));
            return false;
        }

        // 将最后一个Activation的结束y坐标设置为对应消息的下y
        lastActivationInfo.setBottomY(activationBottomY);
        DebugLogger.log(this.getClass(), "setActivationEndY", DebugLogger.getLifelineSeq(activationLifelineSeq), lastActivationInfo.getBottomY().toPlainString());
        return true;
    }

    // 获取上一条消息
    protected MessageInfo getLastMessageInfo() {
        if (messageInfoList.isEmpty()) {
            return null;
        }

        return messageInfoList.get(messageInfoList.size() - 1);
    }

    /**
     * 生成消息信息
     *
     * @param messageInText
     * @param partSeq
     * @return
     */
    protected MessageInfo genMessageInfo(MessageInText messageInText, int partSeq) throws HtmlFormatException {
        // 获得消息起点与终点所在的Lifeline的中间点距离
        int lifelineSeqDistance = messageInText.getEndLifelineSeq() - messageInText.getStartLifelineSeq();
        if (lifelineSeqDistance == 0) {
            // 自调用消息
            lifelineSeqDistance = 1;
        } else if (lifelineSeqDistance < 0) {
            // 返回消息
            lifelineSeqDistance = -lifelineSeqDistance;
        }
        BigDecimal lifelineSpace = confPositionInfo.getLifelineCenterHorizontalSpacing().multiply(BigDecimal.valueOf(lifelineSeqDistance));
        // 获得消息允许的最大宽度
        BigDecimal messageMaxWidth = USDDIUtil.minBigDecimal(lifelineSpace.multiply(USDDIConstants.MESSAGE_MAX_WIDTH_PERCENTAGE_LIFELINE),
                USDDIConstants.MESSAGE_MAX_WIDTH_FIXED);

        // 对消息文本进行格式化
        HtmlFormatResult htmlFormatResult = htmlHandler.formatHtml(messageInText.getMessageText(), messageMaxWidth, confStyleInfo.getTextFontOfLifeline(),
                confStyleInfo.getTextSizeOfLifeline());

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setStartLifelineSeq(messageInText.getStartLifelineSeq());
        messageInfo.setEndLifelineSeq(messageInText.getEndLifelineSeq());
        messageInfo.setLink(messageInText.getLink());
        messageInfo.setPartSeq(partSeq);
        messageInfo.setMessageText(htmlFormatResult.getFormattedHtmlText());
        messageInfo.setHeight(BigDecimal.valueOf(htmlFormatResult.getHeight()));

        return messageInfo;
    }
}
