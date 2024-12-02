package com.adrninistrator.usddi.dto.html;

/**
 * @author adrninistrator
 * @date 2024/9/29
 * @description: html文本换行后的最大宽度与高度
 */
public class HtmlTextMetrics {

    // html文本换行后的最大宽度
    private int maxWidth;

    // html文本换行后的高度
    private int totalHeight;

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getTotalHeight() {
        return totalHeight;
    }

    public void setTotalHeight(int totalHeight) {
        this.totalHeight = totalHeight;
    }
}
