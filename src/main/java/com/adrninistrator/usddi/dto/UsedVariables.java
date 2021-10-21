package com.adrninistrator.usddi.dto;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author adrninistrator
 * @date 2021/9/16
 * @description:
 */
public class UsedVariables {

    // 记录Lifeline
    private List<LifelineInfo> lifelineInfoList = new ArrayList<>();

    // 记录Lifeline用于展示的name及在List中的序号
    private Map<String, Integer> lifelineDisplayedNameMap = new HashMap<>();

    // 记录Lifeline的name别名及在List中的序号
    private Map<String, Integer> lifelineNameAliasMap = new HashMap<>();

    // 记录Message
    private List<MessageInfo> messageInfoList = new ArrayList<>();

    // 记录Activation
    private Map<Integer, List<ActivationInfo>> activationMap = new HashMap<>();

    // Message栈
    private Stack<MessageInStack> messageStack = new Stack<>();

    // 当前处理到的y坐标
    private BigDecimal currentY = BigDecimal.ZERO;

    // 整个Lifeline的高度
    private BigDecimal totalHeight;

    // 最初的起点Lifeline序号
    private Integer firstStartLifelineSeq;

    private static UsedVariables instance;

    public static void reset() {
        instance = new UsedVariables();
    }

    public static UsedVariables getInstance() {
        return instance;
    }

    public void addCurrentY(BigDecimal addValue) {
        this.currentY = currentY.add(addValue);
    }
    //

    public List<LifelineInfo> getLifelineInfoList() {
        return lifelineInfoList;
    }

    public void setLifelineInfoList(List<LifelineInfo> lifelineInfoList) {
        this.lifelineInfoList = lifelineInfoList;
    }

    public Map<String, Integer> getLifelineDisplayedNameMap() {
        return lifelineDisplayedNameMap;
    }

    public void setLifelineDisplayedNameMap(Map<String, Integer> lifelineDisplayedNameMap) {
        this.lifelineDisplayedNameMap = lifelineDisplayedNameMap;
    }

    public Map<String, Integer> getLifelineNameAliasMap() {
        return lifelineNameAliasMap;
    }

    public void setLifelineNameAliasMap(Map<String, Integer> lifelineNameAliasMap) {
        this.lifelineNameAliasMap = lifelineNameAliasMap;
    }

    public List<MessageInfo> getMessageInfoList() {
        return messageInfoList;
    }

    public void setMessageInfoList(List<MessageInfo> messageInfoList) {
        this.messageInfoList = messageInfoList;
    }

    public Map<Integer, List<ActivationInfo>> getActivationMap() {
        return activationMap;
    }

    public void setActivationMap(Map<Integer, List<ActivationInfo>> activationMap) {
        this.activationMap = activationMap;
    }

    public Stack<MessageInStack> getMessageStack() {
        return messageStack;
    }

    public void setMessageStack(Stack<MessageInStack> messageStack) {
        this.messageStack = messageStack;
    }

    public BigDecimal getCurrentY() {
        return currentY;
    }

    public void setCurrentY(BigDecimal currentY) {
        this.currentY = currentY;
    }

    public BigDecimal getTotalHeight() {
        return totalHeight;
    }

    public void setTotalHeight(BigDecimal totalHeight) {
        this.totalHeight = totalHeight;
    }

    public Integer getFirstStartLifelineSeq() {
        return firstStartLifelineSeq;
    }

    public void setFirstStartLifelineSeq(Integer firstStartLifelineSeq) {
        this.firstStartLifelineSeq = firstStartLifelineSeq;
    }
}
