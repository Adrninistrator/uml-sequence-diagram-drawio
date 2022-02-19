package com.adrninistrator.usddi.handler;

import com.adrninistrator.usddi.common.USDDIConstants;
import com.adrninistrator.usddi.dto.activation.ActivationInfo;
import com.adrninistrator.usddi.dto.message.MessageInStack;
import com.adrninistrator.usddi.dto.message.MessageInfo;
import com.adrninistrator.usddi.enums.MessageTypeEnum;
import com.adrninistrator.usddi.handler.base.BaseHandler;
import com.adrninistrator.usddi.logger.DebugLogger;
import com.adrninistrator.usddi.util.USDDIUtil;

import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * @author adrninistrator
 * @date 2021/9/17
 * @description:
 */
public class EndPartHandler extends BaseHandler {

    public boolean handle() {
        DebugLogger.log(this.getClass(), "end part");

        Deque<MessageInStack> messageStack = usedVariables.getMessageStack();
        // 检查栈应为空
        if (!messageStack.isEmpty()) {
            MessageInStack messageInStack = messageStack.peek();

            System.err.println("之前指定的消息还未处理完毕，生命线序号: " +
                    DebugLogger.getLifelineSeq(messageInStack.getStartLifelineSeq()) +
                    USDDIConstants.MESSAGE_REQ_FLAG +
                    DebugLogger.getLifelineSeq(messageInStack.getEndLifelineSeq()));
            return false;
        }

        // 判断Message列表中上一条Message
        if (messageInfoList.isEmpty()) {
            // Message列表为空，什么也不做
            return true;
        }

        // 清空最初的起点Lifeline
        usedVariables.setFirstStartLifelineSeq(null);

        // 处理上一条Message
        if (!handleLastMessage(messageInfoList)) {
            return false;
        }

        // 增加当前的部分序号
        usedVariables.addCurrentPartSeq();

        return true;
    }

    // 处理上一条Message
    private boolean handleLastMessage(List<MessageInfo> messageInfoList) {
        MessageInfo lastMessageInfo = getLastMessageInfo();

        DebugLogger.log(this.getClass(), "handleLastMessage", lastMessageInfo.getMessageType().getType());

        if (MessageTypeEnum.MTE_RSP == lastMessageInfo.getMessageType()) {
            // 上一条为返回Message
            // 为当前Message的终点对应的Lifeline的Activation设置结束y坐标
            // 返回消息，激活下y使用消息的中y
            return setActivationEndY(lastMessageInfo.getMiddleY(), lastMessageInfo.getEndLifelineSeq());
        } else if (MessageTypeEnum.MTE_SELF == lastMessageInfo.getMessageType()) {
            // 上一条为自调用Message
            // 为当前Message的起点对应的Lifeline的Activation设置结束y坐标
            // 返回消息，激活下y使用消息的下y
            return setActivationEndY(lastMessageInfo.getBottomY(), lastMessageInfo.getStartLifelineSeq());
        } else if (MessageTypeEnum.MTE_ASYNC == lastMessageInfo.getMessageType()) {
            // 上一条为异步Message
            // 为当前Message的起点对应的Lifeline的Activation设置结束y坐标
            // 异步消息，激活下y使用消息的中y
            if (!setActivationEndY(lastMessageInfo.getMiddleY(), lastMessageInfo.getStartLifelineSeq())) {
                return false;
            }

            // 检查Message起点Lifeline的Activation List中，判断最后一个Activation
            // 特殊处理：某部分只有一个异步消息，则起点的生命线需要将高度设为非0
            Map<Integer, List<ActivationInfo>> activationMap = usedVariables.getActivationMap();
            List<ActivationInfo> activationInfoList = activationMap.get(lastMessageInfo.getStartLifelineSeq());
            ActivationInfo lastActivationInfo = activationInfoList.get(activationInfoList.size() - 1);
            if (lastActivationInfo.getTopY().compareTo(lastActivationInfo.getBottomY()) == 0) {
                // 若起始y坐标与结束y坐标相同，则将结束y坐标加上Message（及与Lifeline之间）垂直间距
                lastActivationInfo.setBottomY(lastActivationInfo.getBottomY().add(confPositionInfo.getMessageVerticalSpacing()));
                DebugLogger.log(this.getClass(), "setActivationEndY4AsyncStart", DebugLogger.getLifelineSeq(lastMessageInfo.getStartLifelineSeq()),
                        lastActivationInfo.getBottomY().toPlainString());
            }
            return true;
        }

        // 不会执行到此
        System.err.println("未知异常");
        return false;
    }
}
