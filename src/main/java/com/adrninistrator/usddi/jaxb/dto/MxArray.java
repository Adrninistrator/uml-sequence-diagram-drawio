package com.adrninistrator.usddi.jaxb.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author adrninistrator
 * @date 2021/9/21
 * @description:
 */
public class MxArray {
    private List<MxPoint> mxPointList;

    private String as;

    public List<MxPoint> getMxPointList() {
        return mxPointList;
    }

    @XmlElement(name = "mxPoint")
    public void setMxPointList(List<MxPoint> mxPointList) {
        this.mxPointList = mxPointList;
    }

    public String getAs() {
        return as;
    }

    @XmlAttribute(name = "as")
    public void setAs(String as) {
        this.as = as;
    }
}
