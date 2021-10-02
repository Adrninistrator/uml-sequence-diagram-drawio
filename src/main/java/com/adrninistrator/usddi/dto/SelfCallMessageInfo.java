package com.adrninistrator.usddi.dto;

import java.math.BigDecimal;

/**
 * @author adrninistrator
 * @date 2021/9/16
 * @description:
 */
public class SelfCallMessageInfo extends MessageInfo {

    // 点1 x坐标
    private BigDecimal point1X;

    // 点2 x坐标
    private BigDecimal point2X;

    // 点1 y坐标
    private BigDecimal point1Y;

    // 点2 y坐标
    private BigDecimal point2Y;

    public BigDecimal getPoint1X() {
        return point1X;
    }

    public void setPoint1X(BigDecimal point1X) {
        this.point1X = point1X;
    }

    public BigDecimal getPoint2X() {
        return point2X;
    }

    public void setPoint2X(BigDecimal point2X) {
        this.point2X = point2X;
    }

    public BigDecimal getPoint1Y() {
        return point1Y;
    }

    public void setPoint1Y(BigDecimal point1Y) {
        this.point1Y = point1Y;
    }

    public BigDecimal getPoint2Y() {
        return point2Y;
    }

    public void setPoint2Y(BigDecimal point2Y) {
        this.point2Y = point2Y;
    }
}
