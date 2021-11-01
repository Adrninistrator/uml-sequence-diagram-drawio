package com.adrninistrator.usddi.dto;

import java.math.BigDecimal;

/**
 * @author adrninistrator
 * @date 2021/9/15
 * @description:
 */
public class LifelineInfo {

    // Lifeline用于展示的name
    private String displayedName;

    // Lifeline中间点x坐标
    private BigDecimal centerX;

    // Lifeline起点y坐标
    private BigDecimal startY;

    public String getDisplayedName() {
        return displayedName;
    }

    public void setDisplayedName(String displayedName) {
        this.displayedName = displayedName;
    }

    public BigDecimal getCenterX() {
        return centerX;
    }

    public void setCenterX(BigDecimal centerX) {
        this.centerX = centerX;
    }

    public BigDecimal getStartY() {
        return startY;
    }

    public void setStartY(BigDecimal startY) {
        this.startY = startY;
    }
}
