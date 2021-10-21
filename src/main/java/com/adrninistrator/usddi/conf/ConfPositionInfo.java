package com.adrninistrator.usddi.conf;

import java.math.BigDecimal;

/**
 * @author adrninistrator
 * @date 2021/9/15
 * @description:
 */
public class ConfPositionInfo {

    // Lifeline中间点的水平间距
    private BigDecimal lifelineCenterHorizontalSpacing;

    // Lifeline的方框宽度
    private BigDecimal lifelineBoxWidth;

    // Lifeline的方框宽度的一半
    private BigDecimal lifelineBoxWidthHalf;

    // Lifeline的方框高度
    private BigDecimal lifelineBoxHeight;

    // Message（及与Lifeline之间）垂直间距
    private BigDecimal messageVerticalSpacing;

    // Message（及与Lifeline之间）垂直间距的一半
    private BigDecimal messageVerticalSpacingHalf;

    // Message请求及返回的垂直间距
    private BigDecimal rspMessageVerticalSpacing;

    // 自调用Message的水平宽度
    private BigDecimal selfCallHorizontalWidth;

    // 自调用消息的垂直高度
    private BigDecimal selfCallVerticalHeight;

    // Activation的宽度
    private BigDecimal activationWidth;

    // Activation的宽度的一半
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

    public BigDecimal getMessageVerticalSpacingHalf() {
        return messageVerticalSpacingHalf;
    }

    public void setMessageVerticalSpacingHalf(BigDecimal messageVerticalSpacingHalf) {
        this.messageVerticalSpacingHalf = messageVerticalSpacingHalf;
    }

    public BigDecimal getRspMessageVerticalSpacing() {
        return rspMessageVerticalSpacing;
    }

    public void setRspMessageVerticalSpacing(BigDecimal rspMessageVerticalSpacing) {
        this.rspMessageVerticalSpacing = rspMessageVerticalSpacing;
    }

    public BigDecimal getSelfCallHorizontalWidth() {
        return selfCallHorizontalWidth;
    }

    public void setSelfCallHorizontalWidth(BigDecimal selfCallHorizontalWidth) {
        this.selfCallHorizontalWidth = selfCallHorizontalWidth;
    }

    public BigDecimal getSelfCallVerticalHeight() {
        return selfCallVerticalHeight;
    }

    public void setSelfCallVerticalHeight(BigDecimal selfCallVerticalHeight) {
        this.selfCallVerticalHeight = selfCallVerticalHeight;
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

    public static void setInstance(ConfPositionInfo instance) {
        ConfPositionInfo.instance = instance;
    }
}
