package com.adrninistrator.usddi.enums;

import com.adrninistrator.usddi.common.USDDIConstants;

/**
 * @author adrninistrator
 * @date 2021/9/15
 * @description:
 */
public enum MessageTypeEnum {
    MTE_REQ("REQ", USDDIConstants.MESSAGE_REQ_FLAG, "同步请求"),
    MTE_RSP("RSP", USDDIConstants.MESSAGE_RSP_FLAG, "返回"),
    MTE_SELF("SELF", USDDIConstants.MESSAGE_REQ_FLAG, "自调用"),
    MTE_ASYNC("ASYNC", USDDIConstants.MESSAGE_ASYNC_FLAG, "异步请求"),
    ;

    private String type;
    private String flag;
    private String desc;

    MessageTypeEnum(String type, String flag, String desc) {
        this.type = type;
        this.flag = flag;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getFlag() {
        return flag;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return type + "-" + flag + "-" + desc;
    }
}
