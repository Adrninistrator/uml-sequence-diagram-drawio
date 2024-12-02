package com.adrninistrator.usddi.dto.html;

import com.adrninistrator.usddi.common.enums.HtmlFragmentTypeEnum;

/**
 * @author adrninistrator
 * @date 2024/10/1
 * @description: HTML片段
 */
public class HtmlFragment {

    // HTML片段类型
    private final HtmlFragmentTypeEnum type;

    // HTML片段内容
    private final StringBuilder content;

    // 是否为HTML换行元素
    private final boolean wrappingElement;

    public HtmlFragment(HtmlFragmentTypeEnum type, char content, boolean wrappingElement) {
        this(type, new StringBuilder().append(content), wrappingElement);
    }

    public HtmlFragment(HtmlFragmentTypeEnum type, String content, boolean wrappingElement) {
        this(type, new StringBuilder(content), wrappingElement);
    }

    public HtmlFragment(HtmlFragmentTypeEnum type, StringBuilder content, boolean wrappingElement) {
        this.type = type;
        this.content = content;
        this.wrappingElement = wrappingElement;
    }

    @Override
    public String toString() {
        return "type=" + type +
                ", content=" + content +
                ", wrappingElement=" + wrappingElement;
    }

    public HtmlFragmentTypeEnum getType() {
        return type;
    }

    public StringBuilder getContent() {
        return content;
    }

    public boolean isWrappingElement() {
        return wrappingElement;
    }
}
