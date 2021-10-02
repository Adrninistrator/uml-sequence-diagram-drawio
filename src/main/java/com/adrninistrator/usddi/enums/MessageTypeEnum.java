package com.adrninistrator.usddi.enums;

/**
 * @author adrninistrator
 * @date 2021/9/15
 * @description:
 */
public enum MessageTypeEnum {
    MTE_REQ("REQ", "同步请求"),
    MTE_RSP("RSP", "返回"),
    MTE_SELF("SELF", "自调用"),
    MTE_ASYNC("ASYNC", "异步请求"),
    ;

    private String type;

    private String desc;

    MessageTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return type + "-" + desc;
    }
}
