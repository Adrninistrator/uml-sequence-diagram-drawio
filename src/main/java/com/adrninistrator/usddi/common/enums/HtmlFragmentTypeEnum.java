package com.adrninistrator.usddi.common.enums;

/**
 * @author adrninistrator
 * @date 2024/10/1
 * @description: HTML片段类型枚举
 */
public enum HtmlFragmentTypeEnum {
    HFTE_ELEMENT("element", "HTML元素"),
    HFTE_ELEMENT_END("element_end", "HTML元素，结束部分"),
    HFTE_TEXT("text", "HTML文本"),
    HFTE_AUTO_BR("auto_br", "自动添加的换行"),
    ;

    private final String type;
    private final String desc;

    HtmlFragmentTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
