package com.adrninistrator.usddi.jaxb.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author adrninistrator
 * @date 2021/9/18
 * @description:
 */
@XmlType(propOrder = {"root", "dx", "dy", "grid", "gridSize", "guides", "tooltips", "connect", "arrows", "fold", "page", "pageScale", "pageWidth", "pageHeight", "math", "shadow"})
@XmlRootElement(name = "mxGraphModel")
public class MxGraphModel {
    private MxRoot root;

    private String dx;
    private String dy;
    private String grid;
    private String gridSize;
    private String guides;
    private String tooltips;
    private String connect;
    private String arrows;
    private String fold;
    private String page;
    private String pageScale;
    private String pageWidth;
    private String pageHeight;
    private String math;
    private String shadow;

    public String getDx() {
        return dx;
    }

    public MxRoot getRoot() {
        return root;
    }

    @XmlElement(name = "root")
    public void setRoot(MxRoot root) {
        this.root = root;
    }

    @XmlAttribute(name = "dx")
    public void setDx(String dx) {
        this.dx = dx;
    }

    public String getDy() {
        return dy;
    }

    @XmlAttribute(name = "dy")
    public void setDy(String dy) {
        this.dy = dy;
    }

    public String getGrid() {
        return grid;
    }

    @XmlAttribute(name = "grid")
    public void setGrid(String grid) {
        this.grid = grid;
    }

    public String getGridSize() {
        return gridSize;
    }

    @XmlAttribute(name = "gridSize")
    public void setGridSize(String gridSize) {
        this.gridSize = gridSize;
    }

    public String getGuides() {
        return guides;
    }

    @XmlAttribute(name = "guides")
    public void setGuides(String guides) {
        this.guides = guides;
    }

    public String getTooltips() {
        return tooltips;
    }

    @XmlAttribute(name = "tooltips")
    public void setTooltips(String tooltips) {
        this.tooltips = tooltips;
    }

    public String getConnect() {
        return connect;
    }

    @XmlAttribute(name = "connect")
    public void setConnect(String connect) {
        this.connect = connect;
    }

    public String getArrows() {
        return arrows;
    }

    @XmlAttribute(name = "arrows")
    public void setArrows(String arrows) {
        this.arrows = arrows;
    }

    public String getFold() {
        return fold;
    }

    @XmlAttribute(name = "fold")
    public void setFold(String fold) {
        this.fold = fold;
    }

    public String getPage() {
        return page;
    }

    @XmlAttribute(name = "page")
    public void setPage(String page) {
        this.page = page;
    }

    public String getPageScale() {
        return pageScale;
    }

    @XmlAttribute(name = "pageScale")
    public void setPageScale(String pageScale) {
        this.pageScale = pageScale;
    }

    public String getPageWidth() {
        return pageWidth;
    }

    @XmlAttribute(name = "pageWidth")
    public void setPageWidth(String pageWidth) {
        this.pageWidth = pageWidth;
    }

    public String getPageHeight() {
        return pageHeight;
    }

    @XmlAttribute(name = "pageHeight")
    public void setPageHeight(String pageHeight) {
        this.pageHeight = pageHeight;
    }

    public String getMath() {
        return math;
    }

    @XmlAttribute(name = "math")
    public void setMath(String math) {
        this.math = math;
    }

    public String getShadow() {
        return shadow;
    }

    @XmlAttribute(name = "shadow")
    public void setShadow(String shadow) {
        this.shadow = shadow;
    }
}
