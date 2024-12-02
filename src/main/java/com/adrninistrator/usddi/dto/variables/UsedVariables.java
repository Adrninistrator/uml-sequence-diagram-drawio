package com.adrninistrator.usddi.dto.variables;

import com.adrninistrator.usddi.dto.activation.ActivationInfo;
import com.adrninistrator.usddi.dto.description.DescriptionInfo;
import com.adrninistrator.usddi.dto.lifeline.LifelineInfo;
import com.adrninistrator.usddi.dto.message.MessageInStack;
import com.adrninistrator.usddi.dto.message.MessageInfo;
import com.adrninistrator.usddi.logger.DebugLogger;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author adrninistrator
 * @date 2021/9/16
 * @description: 使用的变量
 */
public class UsedVariables {

    // 记录描述
    private DescriptionInfo descriptionInfo = new DescriptionInfo();

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
    private Deque<MessageInStack> messageStack = new ArrayDeque<>();

    // 当前处理到的y坐标
    private BigDecimal currentY = BigDecimal.ZERO;

    // 整个区域的宽度
    private BigDecimal totalWidth;

    // 整个Lifeline的高度
    private BigDecimal lifelineTotalHeight;

    // Lifeline的起始y坐标
    private BigDecimal lifelineStartY = BigDecimal.ZERO;

    // 最初的起点Lifeline序号
    private Integer firstStartLifelineSeq;

    // 当前部分的序号，从0开始
    private int currentPartSeq = 0;

    // 生命线的方框实际宽度
    private BigDecimal lifelineBoxActualWidth;

    // 生命线的方框实际宽度的一半
    private BigDecimal lifelineBoxActualWidthHalf;

    // 生命线的方框实际高度
    private BigDecimal lifelineBoxActualHeight;

    public void addCurrentY(Class<?> clazz, String operate, BigDecimal addValue) {
        DebugLogger.log(clazz, "addCurrentY", operate, addValue.toPlainString());
        this.currentY = currentY.add(addValue);
    }

    public void addCurrentPartSeq() {
        this.currentPartSeq++;
    }

    //
    public DescriptionInfo getDescriptionInfo() {
        return descriptionInfo;
    }

    public void setDescriptionInfo(DescriptionInfo descriptionInfo) {
        this.descriptionInfo = descriptionInfo;
    }

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

    public Deque<MessageInStack> getMessageStack() {
        return messageStack;
    }

    public void setMessageStack(Deque<MessageInStack> messageStack) {
        this.messageStack = messageStack;
    }

    public BigDecimal getCurrentY() {
        return currentY;
    }

    public void setCurrentY(BigDecimal currentY) {
        this.currentY = currentY;
    }

    public BigDecimal getTotalWidth() {
        return totalWidth;
    }

    public void setTotalWidth(BigDecimal totalWidth) {
        this.totalWidth = totalWidth;
    }

    public BigDecimal getLifelineTotalHeight() {
        return lifelineTotalHeight;
    }

    public void setLifelineTotalHeight(BigDecimal lifelineTotalHeight) {
        this.lifelineTotalHeight = lifelineTotalHeight;
    }

    public BigDecimal getLifelineStartY() {
        return lifelineStartY;
    }

    public void setLifelineStartY(BigDecimal lifelineStartY) {
        this.lifelineStartY = lifelineStartY;
    }

    public Integer getFirstStartLifelineSeq() {
        return firstStartLifelineSeq;
    }

    public void setFirstStartLifelineSeq(Integer firstStartLifelineSeq) {
        this.firstStartLifelineSeq = firstStartLifelineSeq;
    }

    public int getCurrentPartSeq() {
        return currentPartSeq;
    }

    public void setCurrentPartSeq(int currentPartSeq) {
        this.currentPartSeq = currentPartSeq;
    }

    public BigDecimal getLifelineBoxActualWidth() {
        return lifelineBoxActualWidth;
    }

    public void setLifelineBoxActualWidth(BigDecimal lifelineBoxActualWidth) {
        this.lifelineBoxActualWidth = lifelineBoxActualWidth;
    }

    public BigDecimal getLifelineBoxActualWidthHalf() {
        return lifelineBoxActualWidthHalf;
    }

    public void setLifelineBoxActualWidthHalf(BigDecimal lifelineBoxActualWidthHalf) {
        this.lifelineBoxActualWidthHalf = lifelineBoxActualWidthHalf;
    }

    public BigDecimal getLifelineBoxActualHeight() {
        return lifelineBoxActualHeight;
    }

    public void setLifelineBoxActualHeight(BigDecimal lifelineBoxActualHeight) {
        this.lifelineBoxActualHeight = lifelineBoxActualHeight;
    }
}
