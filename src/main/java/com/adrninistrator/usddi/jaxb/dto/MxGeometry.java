package com.adrninistrator.usddi.jaxb.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author adrninistrator
 * @date 2021/9/21
 * @description:
 */
@XmlType(propOrder = {"mxPointList", "array", "x", "y", "width", "height", "relative", "as"})
public class MxGeometry {

    private List<MxPoint> mxPointList;
    private MxArray array;

    private String x;
    private String y;
    private String width;
    private String height;
    private String relative;
    private String as;

    public List<MxPoint> getMxPointList() {
        return mxPointList;
    }

    @XmlElement(name = "mxPoint")
    public void setMxPointList(List<MxPoint> mxPointList) {
        this.mxPointList = mxPointList;
    }

    public MxArray getArray() {
        return array;
    }

    @XmlElement(name = "Array")
    public void setArray(MxArray array) {
        this.array = array;
    }

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

    public String getWidth() {
        return width;
    }

    @XmlAttribute(name = "width")
    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    @XmlAttribute(name = "height")
    public void setHeight(String height) {
        this.height = height;
    }

    public String getRelative() {
        return relative;
    }

    @XmlAttribute(name = "relative")
    public void setRelative(String relative) {
        this.relative = relative;
    }

    public String getAs() {
        return as;
    }

    @XmlAttribute(name = "as")
    public void setAs(String as) {
        this.as = as;
    }
}
