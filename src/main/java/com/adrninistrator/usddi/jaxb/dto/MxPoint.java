package com.adrninistrator.usddi.jaxb.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author adrninistrator
 * @date 2021/9/21
 * @description:
 */
@XmlType(propOrder = {"x", "y", "as"})
public class MxPoint {
    private String x;
    private String y;
    private String as;

    public String getX() {
        return x;
    }

    @XmlAttribute(name = "x")
    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    @XmlAttribute(name = "y")
    public void setY(String y) {
        this.y = y;
    }

    public String getAs() {
        return as;
    }

    @XmlAttribute(name = "as")
    public void setAs(String as) {
        this.as = as;
    }
}
