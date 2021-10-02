package com.adrninistrator.usddi.handler.base;

import com.adrninistrator.usddi.conf.ConfPositionInfo;
import com.adrninistrator.usddi.dto.ActivationInfo;
import com.adrninistrator.usddi.dto.MessageInStack;
import com.adrninistrator.usddi.dto.UsedVariables;

import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author adrninistrator
 * @date 2021/9/16
 * @description:
 */
public abstract class BaseHandler {

    protected UsedVariables usedVariables = UsedVariables.getInstance();

    protected ConfPositionInfo confPositionInfo = ConfPositionInfo.getInstance();

    protected Stack<MessageInStack> messageStack = usedVariables.getMessageStack();

    /**
     * 为当前Message的起点或终点对应的Lifeline的Activation设置结束y坐标
     *
     * @param startOrEndLifelineSeq
     * @return
     */
    protected boolean setActivationEndY(Integer startOrEndLifelineSeq) {
        // 在记录Activation的Map中，起/终点Lifeline的Activation List中，判断最后一个Activation
        Map<Integer, List<ActivationInfo>> activationMap = usedVariables.getActivationMap();
        List<ActivationInfo> activationInfoList = activationMap.get(startOrEndLifelineSeq);
        // 最后一个Activation应满足存在，且结束y坐标未设置
        if (activationInfoList == null) {
            System.err.println("未找到对应的Activation信息");
            return false;
        }
        ActivationInfo lastActivationInfo = activationInfoList.get(activationInfoList.size() - 1);
        if (lastActivationInfo.getEndY() != null) {
            System.err.println("最后一个Activation的结束y坐标已设置");
            return false;
        }

        // 将最后一个Activation的结束y坐标设置为当前处理到的y坐标
        lastActivationInfo.setEndY(usedVariables.getCurrentY());
        return true;
    }
}
