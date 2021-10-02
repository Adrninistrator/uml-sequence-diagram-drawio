package com.adrninistrator.usddi.jaxb.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author adrninistrator
 * @date 2021/9/21
 * @description:
 */
@XmlType(propOrder = {"mxGeometry", "id", "value", "style", "vertex", "parent", "edge"})
public class MxCell {

    private MxGeometry mxGeometry;

    private String id;
    private String value;
    private String style;
    private String vertex;
    private String parent;
    private String edge;

    public MxGeometry getMxGeometry() {
        return mxGeometry;
    }

    @XmlElement(name = "mxGeometry")
    public void setMxGeometry(MxGeometry mxGeometry) {
        this.mxGeometry = mxGeometry;
    }

    public String getId() {
        return id;
    }

    @XmlAttribute(name = "id")
    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    @XmlAttribute(name = "value")
    public void setValue(String value) {
        this.value = value;
    }

    public String getStyle() {
        return style;
    }

    @XmlAttribute(name = "style")
    public void setStyle(String style) {
        this.style = style;
    }

    public String getVertex() {
        return vertex;
    }

    @XmlAttribute(name = "vertex")
    public void setVertex(String vertex) {
        this.vertex = vertex;
    }

    public String getParent() {
        return parent;
    }

    @XmlAttribute(name = "parent")
    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getEdge() {
        return edge;
    }

    @XmlAttribute(name = "edge")
    public void setEdge(String edge) {
        this.edge = edge;
    }
}
