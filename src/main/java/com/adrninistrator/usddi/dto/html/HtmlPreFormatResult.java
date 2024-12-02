package com.adrninistrator.usddi.dto.html;

import com.adrninistrator.usddi.common.USDDIConstants;
import com.adrninistrator.usddi.common.enums.HtmlFragmentTypeEnum;
import com.adrninistrator.usddi.logger.DebugLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author adrninistrator
 * @date 2024/10/1
 * @description: HTML文本格式化预处理结果
 */
public class HtmlPreFormatResult {

    // HTML片段列表
    private final List<HtmlFragment> htmlFragmentList = new ArrayList<>();

    // HTML文本每行的最大字体大小列表
    private final List<Integer> maxFontSizePerLineList = new ArrayList<>();

    // HTML文本所有行的最大宽度
    private int htmlTextMaxWidth = 0;

    // 获得HTML片段列表，只读
    public List<HtmlFragment> getHtmlFragmentListReadOnly() {
        return Collections.unmodifiableList(htmlFragmentList);
    }

    // 判断HTML片段列表是否为空
    public boolean isHtmlFragmentListEmpty() {
        return htmlFragmentList.isEmpty();
    }

    // 向HTML片段列表添加元素
    public void addHtmlFragment(HtmlFragment htmlFragment) {
        if (HtmlFragmentTypeEnum.HFTE_AUTO_BR == htmlFragment.getType()) {
            throw new RuntimeException("添加自动换行时需要使用 addHtmlFragmentBr 方法");
        }
        htmlFragmentList.add(htmlFragment);
    }

    // 向HTML片段列表添加换行元素
    public void addHtmlFragmentBr() {
        HtmlFragment htmlFragment = new HtmlFragment(HtmlFragmentTypeEnum.HFTE_AUTO_BR, USDDIConstants.HTML_NEW_LINE, false);
        htmlFragmentList.add(htmlFragment);
    }

    /**
     * 是否是否允许自动添加换行
     *
     * @param autoAddBrDescription 不允许时的原因描述
     * @return true: 允许 false: 不允许
     */
    public boolean checkAllowAutoAddBr(StringBuilder autoAddBrDescription) {
        // 记录最后一个自动添加换行的下标
        int lastAutoAddedBrIndex = -1;
        // 记录最后一个人工指定换行的下标
        int lastManualBrIndex = -1;
        // 记录最后一个HTML文本的下标
        int lastHtmlTextIndex = -1;
        // 从后往前遍历HTML片段列表
        for (int i = htmlFragmentList.size() - 1; i >= 0; i--) {
            HtmlFragment htmlFragment = htmlFragmentList.get(i);
            if (lastAutoAddedBrIndex == -1 && HtmlFragmentTypeEnum.HFTE_AUTO_BR == htmlFragment.getType()) {
                lastAutoAddedBrIndex = i;
            }
            if (lastManualBrIndex == -1 && htmlFragment.isWrappingElement()) {
                lastManualBrIndex = i;
            }
            if (lastHtmlTextIndex == -1 && HtmlFragmentTypeEnum.HFTE_TEXT == htmlFragment.getType()) {
                lastHtmlTextIndex = i;
            }
            if (lastAutoAddedBrIndex != -1 && lastManualBrIndex != -1 && lastHtmlTextIndex != -1) {
                break;
            }
        }
        autoAddBrDescription.setLength(0);
        if (lastHtmlTextIndex == -1) {
            autoAddBrDescription.append("HTML片段列表还未添加过HTML文本，此时不能自动添加换行");
            return false;
        }
        if (lastAutoAddedBrIndex != -1 && lastAutoAddedBrIndex > lastHtmlTextIndex) {
            autoAddBrDescription.append("自动添加换行后还未添加过HTML文本，此时不能自动添加换行");
            return false;
        }
        if (lastManualBrIndex != -1 && lastManualBrIndex > lastHtmlTextIndex) {
            autoAddBrDescription.append("人工指定换行后还未添加过HTML文本，此时不能自动添加换行");
            return false;
        }
        return true;
    }

    // 获得HTML片段列表最后一个元素
    public HtmlFragment getLastHtmlFragment() {
        return htmlFragmentList.get(htmlFragmentList.size() - 1);
    }

    // 尝试设置HTML文本所有行的最大宽度
    public void trySetHtmlTextMaxWidth(int newHtmlTextMaxWidth) {
        if (newHtmlTextMaxWidth > htmlTextMaxWidth) {
            DebugLogger.log(this.getClass(), "设置HTML文本所有行的最大宽度", "修改前", htmlTextMaxWidth, "修改后", newHtmlTextMaxWidth);
            htmlTextMaxWidth = newHtmlTextMaxWidth;
        }
    }

    // 记录HTML文本当前行的最大字体大小
    public void recordMaxFontSizePerLine(int height) {
        maxFontSizePerLineList.add(height);
    }

    // 设置HTML文本最后一行的最大字体大小为0
    public void setLastMaxFontSizeZero() {
        maxFontSizePerLineList.set(maxFontSizePerLineList.size() - 1, 0);
    }

    public List<Integer> getMaxFontSizePerLineListReadOnly() {
        return Collections.unmodifiableList(maxFontSizePerLineList);
    }

    public int getHtmlTextMaxWidth() {
        return htmlTextMaxWidth;
    }
}
