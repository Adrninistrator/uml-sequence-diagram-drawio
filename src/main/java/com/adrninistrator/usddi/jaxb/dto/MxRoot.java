package com.adrninistrator.usddi.jaxb.dto;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author adrninistrator
 * @date 2021/9/21
 * @description:
 */
public class MxRoot {

    private List<MxCell> mxCellList;

    public List<MxCell> getMxCellList() {
        return mxCellList;
    }

    @XmlElement(name = "mxCell")
    public void setMxCellList(List<MxCell> mxCellList) {
        this.mxCellList = mxCellList;
    }
}
