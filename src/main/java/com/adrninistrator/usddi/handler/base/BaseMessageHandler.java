package com.adrninistrator.usddi.handler.base;

import com.adrninistrator.usddi.conf.ConfPositionInfo;
import com.adrninistrator.usddi.conf.ConfStyleInfo;
import com.adrninistrator.usddi.dto.activation.ActivationInfo;
import com.adrninistrator.usddi.dto.lifeline.LifelineInfo;
import com.adrninistrator.usddi.dto.message.MessageInStack;
import com.adrninistrator.usddi.dto.message.MessageInText;
import com.adrninistrator.usddi.dto.message.MessageInfo;
import com.adrninistrator.usddi.dto.variables.UsedVariables;
import com.adrninistrator.usddi.enums.MessageTypeEnum;
import com.adrninistrator.usddi.exceptions.HtmlFormatException;
import com.adrninistrator.usddi.html.HtmlHandler;
import com.adrninistrator.usddi.logger.DebugLogger;
import com.adrninistrator.usddi.util.USDDIUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author adrninistrator
 * @date 2021/9/16
 * @description:
 */
public abstract class BaseMessageHandler extends BaseHandler {

    protected BaseMessageHandler(UsedVariables usedVariables, ConfPositionInfo confPositionInfo, ConfStyleInfo confStyleInfo, HtmlHandler htmlHandler) {
        super(usedVariables, confPositionInfo, confStyleInfo, htmlHandler);
    }

    // 对Message进行处理
    public abstract boolean handleMessage(MessageInText messageInText) throws HtmlFormatException;

    /**
     * 检查栈顶元素
     *
     * @param messageInText
     * @return true: 检查通过，false: 检查不通过
     */
    protected boolean checkStackTop(MessageInText messageInText) {
        if (!messageStack.isEmpty()) {
            // 当栈非空时，当前Message只能以栈顶的终点Lifeline作为起点
            MessageInStack topMessage = messageStack.peek();
            if (!messageInText.getStartLifelineSeq().equals(topMessage.getEndLifelineSeq())) {
                System.err.println("当前消息的起点生命线序号 " + messageInText.getStartLifelineSeq() + " 与上一条消息的终点生命线序号 " + topMessage.getEndLifelineSeq() + " 不同");
                return false;
            }
        }
        return true;
    }

    /**
     * 检查Message起点
     *
     * @param messageInText
     * @return true: 检查通过，false: 检查不通过
     */
    protected boolean checkMessageStart(MessageInText messageInText) {
        if (messageStack.isEmpty()) {
            // 当栈为空时，检查当前Message是否以最初的起点Lifeline为起点
            if (usedVariables.getFirstStartLifelineSeq() == null) {
                // 最初的起点Lifeline为空时，进行设置
                usedVariables.setFirstStartLifelineSeq(messageInText.getStartLifelineSeq());
                return true;
            } else {
                // 最初的起点Lifeline非空时，检查是否与当前Message起点相同
                if (!usedVariables.getFirstStartLifelineSeq().equals(messageInText.getStartLifelineSeq())) {
                    System.err.println("前面的同步请求消息及对应的返回消息已处理完毕，当前消息需要以最初的起点生命线作为起点，对应生命线序号: " + DebugLogger.getLifelineSeq(usedVariables.getFirstStartLifelineSeq()));
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 处理Message位置
     *
     * @param messageInfo
     */
    protected void handleMessagePosition(MessageInfo messageInfo) {
        DebugLogger.logMessageInfo(this.getClass(), "handleMessagePosition", messageInfo);

        // 处理消息的y坐标
        handleMessageY(messageInfo);

        // 处理消息的x坐标
        handleMessageX(messageInfo);
    }

    // 处理消息的y坐标
    private void handleMessageY(MessageInfo messageInfo) {
        // 获取消息的上y
        BigDecimal topY = getMessageTopY(messageInfo);
        messageInfo.setTopY(topY);

        // 消息的下y，等于上y加消息高度
        messageInfo.setBottomY(topY.add(messageInfo.getHeight()));

        // 消息的中y，等于上y加消息高度的一半
        messageInfo.setMiddleY(topY.add(USDDIUtil.getHalfBigDecimal(messageInfo.getHeight())));

        DebugLogger.log(this.getClass(), "handleMessageY", "topY:" + topY.toPlainString(),
                "middleY:" + messageInfo.getMiddleY().toPlainString(),
                "bottomY:" + messageInfo.getBottomY().toPlainString());
    }

    // 处理消息的x坐标
    private void handleMessageX(MessageInfo messageInfo) {
        List<LifelineInfo> lifelineInfoList = usedVariables.getLifelineInfoList();
        // 获取起点与终点Lifeline
        LifelineInfo startLifelineInfo = lifelineInfoList.get(messageInfo.getStartLifelineSeq());
        LifelineInfo endLifelineInfo = lifelineInfoList.get(messageInfo.getEndLifelineSeq());

        if (messageInfo.getStartLifelineSeq() <= messageInfo.getEndLifelineSeq()) {
            // 起点在终点的左边（或自调用消息）
            // 起点x坐标为起点Lifeline中间点的x坐标加上配置中指定的Activation宽度的一半
            messageInfo.setStartX(startLifelineInfo.getCenterX().add(confPositionInfo.getActivationWidthHalf()));
            // 终点x坐标为终点Lifeline中间点的x坐标减去配置中指定的Activation宽度的一半
            messageInfo.setEndX(endLifelineInfo.getCenterX().subtract(confPositionInfo.getActivationWidthHalf()));
        } else {
            // 起点在终点的右边
            // 起点x坐标为起点Lifeline中间点的x坐标减去配置中指定的Activation宽度的一半
            messageInfo.setStartX(startLifelineInfo.getCenterX().subtract(confPositionInfo.getActivationWidthHalf()));
            // 终点x坐标为终点Lifeline中间点的x坐标加上配置中指定的Activation宽度的一半
            messageInfo.setEndX(endLifelineInfo.getCenterX().add(confPositionInfo.getActivationWidthHalf()));
        }

        if (messageInfo.getMessageType() == MessageTypeEnum.MTE_SELF) {
            // 对于自调用消息，起点x坐标对应左x坐标，终点x坐标对应右x坐标
            // 终点x等于起点x加上自调用消息宽度
            messageInfo.setEndX(messageInfo.getStartX().add(confPositionInfo.getSelfCallHorizontalWidth()));
        }

        DebugLogger.log(this.getClass(), "handleMessageX", "startX:" + messageInfo.getStartX().toPlainString(),
                "endX:" + messageInfo.getEndX().toPlainString());
    }

    // 获取消息的上y
    private BigDecimal getMessageTopY(MessageInfo messageInfo) {
        if (messageInfoList.isEmpty()) {
            // 当前消息为第一条消息
            // 上y设置为当前y坐标加上消息（及与生命线之间）垂直间距
            DebugLogger.log(this.getClass(), "getMessageTopY", "first message of all");
            return usedVariables.getCurrentY().add(confPositionInfo.getMessageVerticalSpacing());
        }

        // 获取上一条消息
        MessageInfo lastMessageInfo = getLastMessageInfo();
        // 获取上一条消息的下y
        BigDecimal lastMessageBottomY = USDDIUtil.getLastMessageBottomY(lastMessageInfo);

        if (lastMessageInfo.getPartSeq() != messageInfo.getPartSeq()) {
            // 当前消息与上一条消息不属于同一部分，即当前消息为当前部分的第一条消息
            DebugLogger.log(this.getClass(), "getMessageTopY", "first message of part", String.valueOf(messageInfo.getPartSeq()));

            // 当前消息上y应等于：上一条消息的下y，加上“消息（及与生命线之间）垂直间距”，再加上“两个部分之间的额外垂直间距”
            return lastMessageBottomY.add(confPositionInfo.getMessageVerticalSpacing()).add(confPositionInfo.getPartsExtraVerticalSpacing());
        }

        // 当前消息不是当前部分的第一条消息
        if (checkTwoMessageXNoCoincide(messageInfo, lastMessageInfo)) {
            // 当前消息与上一条消息x坐标没有重合
            // 当前消息的上y等于：上一条消息的下y
            DebugLogger.log(this.getClass(), "getMessageTopY", "not first message, x not coincide", String.valueOf(messageInfo.getPartSeq()));
            return lastMessageBottomY;
        }

        // 当前消息与上一条消息x坐标有重合
        // 当前消息的上y等于：上一条消息的下y，再加上“消息（及与生命线之间）垂直间距”
        DebugLogger.log(this.getClass(), "getMessageTopY", "not first message, x coincide", String.valueOf(messageInfo.getPartSeq()));
        return lastMessageBottomY.add(confPositionInfo.getMessageVerticalSpacing());
    }

    /**
     * 判断当前消息与上一条消息在x坐标是否没有重合
     *
     * @param messageInfo
     * @param lastMessageInfo
     * @return true: 没有重合，false: 有重合
     */
    private boolean checkTwoMessageXNoCoincide(MessageInfo messageInfo, MessageInfo lastMessageInfo) {
        return checkMessageInRightPosition(messageInfo, lastMessageInfo) || checkMessageInRightPosition(lastMessageInfo, messageInfo);
    }

    /**
     * 判断消息1的起点与终点的生命线序号，是否都大于等于消息2的起点与终点的生命线序号，即消息1整个在消息2的右边
     *
     * @param messageInfo1
     * @param messageInfo2
     * @return
     */
    private boolean checkMessageInRightPosition(MessageInfo messageInfo1, MessageInfo messageInfo2) {
        // 对于自调用消息，在比较时需要将终点序号设置为起点序号加1
        int messageInfo1EndSeq = messageInfo1.getMessageType() == MessageTypeEnum.MTE_SELF ? messageInfo1.getStartLifelineSeq() + 1 : messageInfo1.getEndLifelineSeq();
        int messageInfo2EndSeq = messageInfo2.getMessageType() == MessageTypeEnum.MTE_SELF ? messageInfo2.getStartLifelineSeq() + 1 : messageInfo2.getEndLifelineSeq();

        int messageInfo2MaxSeq = Math.max(messageInfo2.getStartLifelineSeq(), messageInfo2EndSeq);
        return messageInfo1.getStartLifelineSeq() >= messageInfo2MaxSeq && messageInfo1EndSeq >= messageInfo2MaxSeq;
    }

    /**
     * 尝试为起点Lifeline增加Activation
     *
     * @param messageInfo
     */
    protected void tryAddActivation4StartLifeline(MessageInfo messageInfo) {
        Map<Integer, List<ActivationInfo>> activationMap = usedVariables.getActivationMap();

        // 在记录Activation的Map中，起点Lifeline的Activation List中，判断最后一个Activation
        List<ActivationInfo> startActivationInfoList = activationMap.computeIfAbsent(messageInfo.getStartLifelineSeq(), k -> new ArrayList<>());
        if (startActivationInfoList.isEmpty() || startActivationInfoList.get(startActivationInfoList.size() - 1).getBottomY() != null) {
            // 若Activation不存在，或者最后一个Activation的结束y坐标已设置，则增加一个Activation
            ActivationInfo startActivationInfo = new ActivationInfo();
            startActivationInfoList.add(startActivationInfo);

            if (messageInfo.getMessageType() != MessageTypeEnum.MTE_SELF) {
                // 非自调用消息，激活起始y坐标为消息的中y
                startActivationInfo.setTopY(messageInfo.getMiddleY());
            } else {
                // 自调用消息，激活起始y坐标为消息的上y
                startActivationInfo.setTopY(messageInfo.getTopY());
            }

            startActivationInfo.setBottomY(null);
            DebugLogger.logActivation(this.getClass(), "addActivation4StartLifeline", messageInfo.getStartLifelineSeq(), startActivationInfo);
        }
    }
}
