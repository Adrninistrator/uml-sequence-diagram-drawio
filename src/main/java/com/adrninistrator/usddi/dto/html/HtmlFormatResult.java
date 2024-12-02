package com.adrninistrator.usddi.dto.html;

/**
 * @author adrninistrator
 * @date 2024/10/2
 * @description: HTML文本格式化结果
 */
public class HtmlFormatResult {
    // 格式化后的HTML文本
    private String formattedHtmlText;

    // HTML文本宽度
    private int width;

    // HTML文本高度
    private int height;

    @Override
    public String toString() {
        return "HtmlFormatResult{" +
                "formattedHtmlText='" + formattedHtmlText + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public String getFormattedHtmlText() {
        return formattedHtmlText;
    }

    public void setFormattedHtmlText(String formattedHtmlText) {
        this.formattedHtmlText = formattedHtmlText;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
