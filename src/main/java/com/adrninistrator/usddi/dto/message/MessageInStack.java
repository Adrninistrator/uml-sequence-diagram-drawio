package com.adrninistrator.usddi.dto.message;

/**
 * @author adrninistrator
 * @date 2021/9/15
 * @description:
 */
public class MessageInStack {

    // 起点Lifeline的序号
    private Integer startLifelineSeq;

    // 终点Lifeline的序号
    private Integer endLifelineSeq;

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
