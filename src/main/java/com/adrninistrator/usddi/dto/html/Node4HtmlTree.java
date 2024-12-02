package com.adrninistrator.usddi.dto.html;

import org.jsoup.nodes.Node;

/**
 * @author adrninistrator
 * @date 2024/9/28
 * @description:
 */
public class Node4HtmlTree {

    // 当前处理的HTML节点
    private Node node;

    // 当前处理的HTML节点的子节点下标
    private int childNodeIndex = -1;

    public Node4HtmlTree(Node node) {
        this.node = node;
    }

    /**
     * 获取当前处理的HTML节点的子节点
     *
     * @return
     */
    public Node getCurrentChildNode() {
        if (childNodeIndex < 0) {
            return node.childNode(0);
        }

        return node.childNode(childNodeIndex);
    }

    /**
     * 增加当前处理的HTML节点的子节点序号
     *
     * @return true: 增加成功 false: 已处理到列表最后一个元素，增加失败
     */
    public boolean addChildNodeIndex() {
        if (childNodeIndex + 1 < node.childNodeSize()) {
            childNodeIndex++;
            return true;
        }
        return false;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public int getChildNodeIndex() {
        return childNodeIndex;
    }

    public void setChildNodeIndex(int childNodeIndex) {
        this.childNodeIndex = childNodeIndex;
    }
}
