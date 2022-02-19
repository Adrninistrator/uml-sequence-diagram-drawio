package com.adrninistrator.usddi.dto.activation;

import java.math.BigDecimal;

/**
 * @author adrninistrator
 * @date 2021/9/15
 * @description:
 */
public class ActivationInfo {

    // 上y坐标
    private BigDecimal topY;

    // 下y坐标
    private BigDecimal bottomY;

    public BigDecimal getTopY() {
        return topY;
    }

    public void setTopY(BigDecimal topY) {
        this.topY = topY;
    }

    public BigDecimal getBottomY() {
        return bottomY;
    }

    public void setBottomY(BigDecimal bottomY) {
        this.bottomY = bottomY;
    }
}
