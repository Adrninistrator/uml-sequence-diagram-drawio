package com.adrninistrator.usddi.conf;

import java.math.BigDecimal;

/**
 * @author adrninistrator
 * @date 2021/9/15
 * @description:
 */
public class ConfPositionInfo {

    // 生命线中间点的水平间距
    private BigDecimal lifelineCenterHorizontalSpacing;

    // 生命线的方框宽度
    private BigDecimal lifelineBoxWidth;

    // 生命线的方框宽度的一半
    private BigDecimal lifelineBoxWidthHalf;

    // 生命线的方框高度
    private BigDecimal lifelineBoxHeight;

    // 消息（及与生命线之间）垂直间距
    private BigDecimal messageVerticalSpacing;

    // 自调用消息的水平宽度
    private BigDecimal selfCallHorizontalWidth;

    // 激活的宽度
    private BigDecimal activationWidth;

    // 激活的宽度的一半
    private BigDecimal activationWidthHalf;

    // 两个部分之间的额外垂直间距
    private BigDecimal partsExtraVerticalSpacing;

    private static ConfPositionInfo instance = new ConfPositionInfo();

    public static ConfPositionInfo getInstance() {
        return instance;
    }
    //

    public BigDecimal getLifelineCenterHorizontalSpacing() {
        return lifelineCenterHorizontalSpacing;
    }

    public void setLifelineCenterHorizontalSpacing(BigDecimal lifelineCenterHorizontalSpacing) {
        this.lifelineCenterHorizontalSpacing = lifelineCenterHorizontalSpacing;
    }

    public BigDecimal getLifelineBoxWidth() {
        return lifelineBoxWidth;
    }

    public void setLifelineBoxWidth(BigDecimal lifelineBoxWidth) {
        this.lifelineBoxWidth = lifelineBoxWidth;
    }

    public BigDecimal getLifelineBoxWidthHalf() {
        return lifelineBoxWidthHalf;
    }

    public void setLifelineBoxWidthHalf(BigDecimal lifelineBoxWidthHalf) {
        this.lifelineBoxWidthHalf = lifelineBoxWidthHalf;
    }

    public BigDecimal getLifelineBoxHeight() {
        return lifelineBoxHeight;
    }

    public void setLifelineBoxHeight(BigDecimal lifelineBoxHeight) {
        this.lifelineBoxHeight = lifelineBoxHeight;
    }

    public BigDecimal getMessageVerticalSpacing() {
        return messageVerticalSpacing;
    }

    public void setMessageVerticalSpacing(BigDecimal messageVerticalSpacing) {
        this.messageVerticalSpacing = messageVerticalSpacing;
    }

    public BigDecimal getSelfCallHorizontalWidth() {
        return selfCallHorizontalWidth;
    }

    public void setSelfCallHorizontalWidth(BigDecimal selfCallHorizontalWidth) {
        this.selfCallHorizontalWidth = selfCallHorizontalWidth;
    }

    public BigDecimal getActivationWidth() {
        return activationWidth;
    }

    public void setActivationWidth(BigDecimal activationWidth) {
        this.activationWidth = activationWidth;
    }

    public BigDecimal getActivationWidthHalf() {
        return activationWidthHalf;
    }

    public void setActivationWidthHalf(BigDecimal activationWidthHalf) {
        this.activationWidthHalf = activationWidthHalf;
    }

    public BigDecimal getPartsExtraVerticalSpacing() {
        return partsExtraVerticalSpacing;
    }

    public void setPartsExtraVerticalSpacing(BigDecimal partsExtraVerticalSpacing) {
        this.partsExtraVerticalSpacing = partsExtraVerticalSpacing;
    }
}
