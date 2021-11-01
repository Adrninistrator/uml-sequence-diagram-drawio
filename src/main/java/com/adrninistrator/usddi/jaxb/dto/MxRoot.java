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

    private List<UserObject> userObjectList;

    public List<MxCell> getMxCellList() {
        return mxCellList;
    }

    @XmlElement(name = "mxCell")
    public void setMxCellList(List<MxCell> mxCellList) {
        this.mxCellList = mxCellList;
    }

    public List<UserObject> getUserObjectList() {
        return userObjectList;
    }

    @XmlElement(name = "UserObject")
    public void setUserObjectList(List<UserObject> userObjectList) {
        this.userObjectList = userObjectList;
    }
}
