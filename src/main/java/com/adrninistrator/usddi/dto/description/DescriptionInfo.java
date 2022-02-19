package com.adrninistrator.usddi.dto.description;

/**
 * @author adrninistrator
 * @date 2021/10/28
 * @description:
 */
public class DescriptionInfo {

    // 启用
    private boolean used;

    // 描述内容
    private String description;

    // 链接
    private String link;

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
