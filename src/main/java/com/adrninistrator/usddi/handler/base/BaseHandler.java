package com.adrninistrator.usddi.handler.base;

import com.adrninistrator.usddi.conf.ConfPositionInfo;
import com.adrninistrator.usddi.dto.activation.ActivationInfo;
import com.adrninistrator.usddi.dto.message.MessageInStack;
import com.adrninistrator.usddi.dto.message.MessageInfo;
import com.adrninistrator.usddi.dto.variables.UsedVariables;
import com.adrninistrator.usddi.logger.DebugLogger;

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

    protected UsedVariables usedVariables = UsedVariables.getInstance();

    protected ConfPositionInfo confPositionInfo = ConfPositionInfo.getInstance();

    protected List<MessageInfo> messageInfoList = usedVariables.getMessageInfoList();

    protected Deque<MessageInStack> messageStack = usedVariables.getMessageStack();

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
}
