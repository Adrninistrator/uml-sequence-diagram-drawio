package com.adrninistrator.usddi.dto.html;

/**
 * @author adrninistrator
 * @date 2024/10/2
 * @description: 与大小相关的字体属性
 */
public class FontAttributesAboutSize {
    // 字体名称
    private String fontName;

    // 字体大小
    private int fontSize;

    // 是否加粗
    private boolean bold;

    @Override
    public String toString() {
        return "fontName='" + fontName +
                ", fontSize=" + fontSize +
                ", bold=" + bold;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }
}
