package com.adrninistrator.usddi.conf;

import java.math.BigDecimal;

/**
 * @author adrninistrator
 * @date 2021/9/24
 * @description:
 */
public class ConfStyleInfo {
    // 自动为消息添加序号
    private boolean messageAutoSeq;

    // 线条宽度-Lifeline
    private BigDecimal lineWidthOfLifeline;

    // 线条宽度-Activation
    private BigDecimal lineWidthOfActivation;

    // 线条宽度-Message
    private BigDecimal lineWidthOfMessage;

    // 线条颜色-Lifeline
    private String lineColorOfLifeline;

    // 线条颜色-Activation
    private String lineColorOfActivation;

    // 线条颜色-Message
    private String lineColorOfMessage;

    // 方框背景颜色-Lifeline
    private String boxColorOfLifeline;

    // 方框背景颜色-Activation
    private String boxColorOfActivation;

    // 文字字体-Lifeline
    private String textFontOfLifeline;

    // 文字字体-Message
    private String textFontOfMessage;

    // 文字大小-Lifeline
    private Integer textSizeOfLifeline;

    // 文字大小-Message
    private Integer textSizeOfMessage;

    // 文字颜色-Lifeline
    private String textColorOfLifeline;

    // 文字颜色-Message
    private String textColorOfMessage;

    public boolean isMessageAutoSeq() {
        return messageAutoSeq;
    }

    public void setMessageAutoSeq(boolean messageAutoSeq) {
        this.messageAutoSeq = messageAutoSeq;
    }

    public BigDecimal getLineWidthOfLifeline() {
        return lineWidthOfLifeline;
    }

    public void setLineWidthOfLifeline(BigDecimal lineWidthOfLifeline) {
        this.lineWidthOfLifeline = lineWidthOfLifeline;
    }

    public BigDecimal getLineWidthOfActivation() {
        return lineWidthOfActivation;
    }

    public void setLineWidthOfActivation(BigDecimal lineWidthOfActivation) {
        this.lineWidthOfActivation = lineWidthOfActivation;
    }

    public BigDecimal getLineWidthOfMessage() {
        return lineWidthOfMessage;
    }

    public void setLineWidthOfMessage(BigDecimal lineWidthOfMessage) {
        this.lineWidthOfMessage = lineWidthOfMessage;
    }

    public String getLineColorOfLifeline() {
        return lineColorOfLifeline;
    }

    public void setLineColorOfLifeline(String lineColorOfLifeline) {
        this.lineColorOfLifeline = lineColorOfLifeline;
    }

    public String getLineColorOfActivation() {
        return lineColorOfActivation;
    }

    public void setLineColorOfActivation(String lineColorOfActivation) {
        this.lineColorOfActivation = lineColorOfActivation;
    }

    public String getLineColorOfMessage() {
        return lineColorOfMessage;
    }

    public void setLineColorOfMessage(String lineColorOfMessage) {
        this.lineColorOfMessage = lineColorOfMessage;
    }

    public String getBoxColorOfLifeline() {
        return boxColorOfLifeline;
    }

    public void setBoxColorOfLifeline(String boxColorOfLifeline) {
        this.boxColorOfLifeline = boxColorOfLifeline;
    }

    public String getBoxColorOfActivation() {
        return boxColorOfActivation;
    }

    public void setBoxColorOfActivation(String boxColorOfActivation) {
        this.boxColorOfActivation = boxColorOfActivation;
    }

    public String getTextFontOfLifeline() {
        return textFontOfLifeline;
    }

    public void setTextFontOfLifeline(String textFontOfLifeline) {
        this.textFontOfLifeline = textFontOfLifeline;
    }

    public String getTextFontOfMessage() {
        return textFontOfMessage;
    }

    public void setTextFontOfMessage(String textFontOfMessage) {
        this.textFontOfMessage = textFontOfMessage;
    }

    public Integer getTextSizeOfLifeline() {
        return textSizeOfLifeline;
    }

    public void setTextSizeOfLifeline(Integer textSizeOfLifeline) {
        this.textSizeOfLifeline = textSizeOfLifeline;
    }

    public Integer getTextSizeOfMessage() {
        return textSizeOfMessage;
    }

    public void setTextSizeOfMessage(Integer textSizeOfMessage) {
        this.textSizeOfMessage = textSizeOfMessage;
    }

    public String getTextColorOfLifeline() {
        return textColorOfLifeline;
    }

    public void setTextColorOfLifeline(String textColorOfLifeline) {
        this.textColorOfLifeline = textColorOfLifeline;
    }

    public String getTextColorOfMessage() {
        return textColorOfMessage;
    }

    public void setTextColorOfMessage(String textColorOfMessage) {
        this.textColorOfMessage = textColorOfMessage;
    }
}
