package com.adrninistrator.usddi.dto;

import com.adrninistrator.usddi.enums.MessageTypeEnum;

/**
 * @author adrninistrator
 * @date 2021/9/16
 * @description:
 */
public class MessageInText {

    // 起点Lifeline的序号
    private Integer startLifelineSeq;

    // 终点Lifeline的序号
    private Integer endLifelineSeq;

    // Message的文字
    private String messageText;

    // Message类型
    private MessageTypeEnum messageType;

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

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public MessageTypeEnum getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageTypeEnum messageType) {
        this.messageType = messageType;
    }
}
