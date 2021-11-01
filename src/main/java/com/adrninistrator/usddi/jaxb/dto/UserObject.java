package com.adrninistrator.usddi.jaxb.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author adrninistrator
 * @date 2021/10/27
 * @description:
 */
@XmlType(propOrder = {"mxCell", "id", "label", "link"})
public class UserObject {

    private String id;
    private String label;
    private String link;

    private MxCell mxCell;

    public MxCell getMxCell() {
        return mxCell;
    }

    @XmlElement(name = "mxCell")
    public void setMxCell(MxCell mxCell) {
        this.mxCell = mxCell;
    }

    public String getId() {
        return id;
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    @XmlAttribute(name = "label")
    public void setLabel(String label) {
        this.label = label;
    }

    public String getLink() {
        return link;
    }

    @XmlAttribute(name = "link")
    public void setLink(String link) {
        this.link = link;
    }
}
