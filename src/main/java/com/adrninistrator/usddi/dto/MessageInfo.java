package com.adrninistrator.usddi.dto;

import com.adrninistrator.usddi.enums.MessageTypeEnum;

import java.math.BigDecimal;

/**
 * @author adrninistrator
 * @date 2021/9/15
 * @description:
 */
public class MessageInfo {

    // Message类型
    private MessageTypeEnum messageType;

    // Message文字
    private String messageText;

    // 起点x坐标
    private BigDecimal startX;

    // 终点x坐标
    private BigDecimal endX;

    // 起点y坐标
    private BigDecimal startY;

    // 终点y坐标
    private BigDecimal endY;

    // 起点Lifeline的序号
    private Integer startLifelineSeq;

    // 终点Lifeline的序号
    private Integer endLifelineSeq;

    public static MessageInfo genFromMessageInText(MessageInText messageInText) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setMessageText(messageInText.getMessageText());
        messageInfo.setStartLifelineSeq(messageInText.getStartLifelineSeq());
        messageInfo.setEndLifelineSeq(messageInText.getEndLifelineSeq());
        return messageInfo;
    }
    //

    public MessageTypeEnum getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageTypeEnum messageType) {
        this.messageType = messageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public BigDecimal getStartX() {
        return startX;
    }

    public void setStartX(BigDecimal startX) {
        this.startX = startX;
    }

    public BigDecimal getEndX() {
        return endX;
    }

    public void setEndX(BigDecimal endX) {
        this.endX = endX;
    }

    public BigDecimal getStartY() {
        return startY;
    }

    public void setStartY(BigDecimal startY) {
        this.startY = startY;
    }

    public BigDecimal getEndY() {
        return endY;
    }

    public void setEndY(BigDecimal endY) {
        this.endY = endY;
    }

    public Integer getStartLifelineSeq() {
        return startLifelineSeq;
    }

    public void setStartLifelineSeq(Integer startLifelineSeq) {
        this.startLifelineSeq = startLifelineSeq;
    }

    public Integer getEndLifelineSeq() {
        return endLifelineSeq;
    }

    public void setEndLifelineSeq(Integer endLifelineSeq) {
        this.endLifelineSeq = endLifelineSeq;
    }
}
