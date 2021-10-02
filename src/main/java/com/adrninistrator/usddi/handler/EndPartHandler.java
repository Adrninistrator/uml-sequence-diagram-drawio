package com.adrninistrator.usddi.handler;

import com.adrninistrator.usddi.dto.ActivationInfo;
import com.adrninistrator.usddi.dto.MessageInStack;
import com.adrninistrator.usddi.dto.MessageInfo;
import com.adrninistrator.usddi.enums.MessageTypeEnum;
import com.adrninistrator.usddi.handler.base.BaseHandler;

import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author adrninistrator
 * @date 2021/9/17
 * @description:
 */
public class EndPartHandler extends BaseHandler {

    public boolean handle() {
        Stack<MessageInStack> messageStack = usedVariables.getMessageStack();
        // 检查栈应为空
        if (!messageStack.isEmpty()) {
            System.err.println("之前指定的Message还未处理完毕");
            return false;
        }

        // 判断Message列表中上一条Message
        List<MessageInfo> messageInfoList = usedVariables.getMessageInfoList();
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

        // 当前处理到的y坐标，加上两个部分之间的额外垂直间距
        if (confPositionInfo.getPartsExtraVerticalSpacing() != null) {
            usedVariables.addCurrentY(confPositionInfo.getPartsExtraVerticalSpacing());
        }

        return true;
    }

    // 处理上一条Message
    private boolean handleLastMessage(List<MessageInfo> messageInfoList) {
        MessageInfo lastMessageInfo = messageInfoList.get(messageInfoList.size() - 1);
        if (MessageTypeEnum.MTE_RSP == lastMessageInfo.getMessageType()) {
            // 上一条为返回Message
            // 为当前Message的终点对应的Lifeline的Activation设置结束y坐标
            return setActivationEndY(lastMessageInfo.getEndLifelineSeq());
        } else if (MessageTypeEnum.MTE_SELF == lastMessageInfo.getMessageType()) {
            // 上一条为自调用Message
            // 为当前Message的起点对应的Lifeline的Activation设置结束y坐标
            return setActivationEndY(lastMessageInfo.getStartLifelineSeq());
        } else if (MessageTypeEnum.MTE_ASYNC == lastMessageInfo.getMessageType()) {
            // 上一条为异步Message
            // 为当前Message的起点对应的Lifeline的Activation设置结束y坐标
            if (!setActivationEndY(lastMessageInfo.getStartLifelineSeq())) {
                return false;
            }

            // 检查Message起点Lifeline的Activation List中，判断最后一个Activation
            Map<Integer, List<ActivationInfo>> activationMap = usedVariables.getActivationMap();
            List<ActivationInfo> activationInfoList = activationMap.get(lastMessageInfo.getStartLifelineSeq());
            ActivationInfo lastActivationInfo = activationInfoList.get(activationInfoList.size() - 1);
            if (lastActivationInfo.getStartY().compareTo(lastActivationInfo.getEndY()) == 0) {
                // 若起始y坐标与结束y坐标相同，则将结束y坐标加上Message（及与Lifeline之间）垂直间距的一半
                lastActivationInfo.setEndY(lastActivationInfo.getEndY().add(confPositionInfo.getMessageVerticalSpacingHalf()));
            }
            return true;
        }

        // 不会执行到此
        System.err.println("未知异常");
        return false;
    }
}
