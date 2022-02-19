package com.adrninistrator.usddi.dto.message;

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

    // 起点Lifeline的序号
    private Integer startLifelineSeq;

    // 终点Lifeline的序号
    private Integer endLifelineSeq;

    // 上y坐标
    private BigDecimal topY;

    // 中y坐标
    private BigDecimal middleY;

    // 下y坐标
    private BigDecimal bottomY;

    // 高度
    private BigDecimal height;

    // 链接
    private String link;

    // 所在部分的序号
    private int partSeq;

    // 终点对应的激活下y坐标
    private BigDecimal asyncMessageEndActivationBottomY;

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

    public BigDecimal getTopY() {
        return topY;
    }

    public void setTopY(BigDecimal topY) {
        this.topY = topY;
    }

    public BigDecimal getMiddleY() {
        return middleY;
    }

    public void setMiddleY(BigDecimal middleY) {
        this.middleY = middleY;
    }

    public BigDecimal getBottomY() {
        return bottomY;
    }

    public void setBottomY(BigDecimal bottomY) {
        this.bottomY = bottomY;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getPartSeq() {
        return partSeq;
    }

    public void setPartSeq(int partSeq) {
        this.partSeq = partSeq;
    }

    public BigDecimal getAsyncMessageEndActivationBottomY() {
        return asyncMessageEndActivationBottomY;
    }

    public void setAsyncMessageEndActivationBottomY(BigDecimal asyncMessageEndActivationBottomY) {
        this.asyncMessageEndActivationBottomY = asyncMessageEndActivationBottomY;
    }
}
