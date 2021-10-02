package com.adrninistrator.usddi.dto;

import java.math.BigDecimal;

/**
 * @author adrninistrator
 * @date 2021/9/15
 * @description:
 */
public class ActivationInfo {

    // 起点y坐标
    private BigDecimal startY;

    // 终点y坐标
    private BigDecimal endY;

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
}
