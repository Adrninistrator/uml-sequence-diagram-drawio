package com.adrninistrator.usddi.dto;

/**
 * @author adrninistrator
 * @date 2021/9/29
 * @description:
 */
public class LifelineName {

    // Lifeline用于展示的name
    private String displayedName;

    // Lifeline的name别名
    private String nameAlias;

    public String getDisplayedName() {
        return displayedName;
    }

    public void setDisplayedName(String displayedName) {
        this.displayedName = displayedName;
    }

    public String getNameAlias() {
        return nameAlias;
    }

    public void setNameAlias(String nameAlias) {
        this.nameAlias = nameAlias;
    }
}
