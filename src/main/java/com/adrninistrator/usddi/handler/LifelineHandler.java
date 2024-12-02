package com.adrninistrator.usddi.handler;

import com.adrninistrator.usddi.common.USDDIConstants;
import com.adrninistrator.usddi.conf.ConfPositionInfo;
import com.adrninistrator.usddi.conf.ConfStyleInfo;
import com.adrninistrator.usddi.dto.lifeline.LifelineInfo;
import com.adrninistrator.usddi.dto.lifeline.LifelineName;
import com.adrninistrator.usddi.dto.variables.UsedVariables;
import com.adrninistrator.usddi.handler.base.BaseHandler;
import com.adrninistrator.usddi.html.HtmlHandler;
import com.adrninistrator.usddi.logger.DebugLogger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author adrninistrator
 * @date 2021/9/16
 * @description:
 */
public class LifelineHandler extends BaseHandler {

    public LifelineHandler(UsedVariables usedVariables, ConfPositionInfo confPositionInfo, ConfStyleInfo confStyleInfo, HtmlHandler htmlHandler) {
        super(usedVariables, confPositionInfo, confStyleInfo, htmlHandler);
    }

    // 添加Lifeline
    public LifelineName addLifeline(String text) {
        DebugLogger.log(this.getClass(), "addLifeline", text);

        // 获得Lifeline的name及alias
        LifelineName lifelineName = getLifelineName(text);
        if (lifelineName == null) {
            return null;
        }

        String displayedName = lifelineName.getDisplayedName();
        String nameAlias = lifelineName.getNameAlias();

        // 判断当前用于展示的name是否已记录
        Map<String, Integer> lifelineDisplayedNameMap = usedVariables.getLifelineDisplayedNameMap();
        if (lifelineDisplayedNameMap.get(displayedName) != null) {
            System.err.println("指定的生命线用于展示的名称重复: " + displayedName);
            return null;
        }

        // 判断当前用于展示的name是否已记录
        Map<String, Integer> lifelineNameAliasMap = usedVariables.getLifelineNameAliasMap();
        if (nameAlias != null && lifelineNameAliasMap.get(nameAlias) != null) {
            System.err.println("指定的生命线的别名重复: " + nameAlias);
            return null;
        }

        // 记录Lifeline
        List<LifelineInfo> lifelineInfoList = usedVariables.getLifelineInfoList();

        LifelineInfo lifelineInfo = new LifelineInfo();
        lifelineInfo.setDisplayedName(displayedName);

        int currentLifelineSeq = lifelineInfoList.size();

        // Lifeline的起始y坐标
        lifelineInfo.setStartY(usedVariables.getLifelineStartY());
        lifelineInfoList.add(lifelineInfo);

        // 记录Lifeline用于展示的name及在List中的序号
        lifelineDisplayedNameMap.put(displayedName, currentLifelineSeq);

        if (nameAlias != null) {
            // 记录Lifeline的name别名及在List中的序号
            lifelineNameAliasMap.put(nameAlias, currentLifelineSeq);
        }

        return lifelineName;
    }

    // 设置Lifeline的中间点x坐标
    public void setLifelineCenterX() {
        List<LifelineInfo> lifelineInfoList = usedVariables.getLifelineInfoList();
        for (int i = 0; i < lifelineInfoList.size(); i++) {
            LifelineInfo lifelineInfo = lifelineInfoList.get(i);
            // 中间点x坐标 = Lifeline方框宽度的1/2 + (当前Lifeline序号-1) * Lifeline中间点的水平间距
            lifelineInfo.setCenterX(usedVariables.getLifelineBoxActualWidthHalf().add(
                    confPositionInfo.getLifelineCenterHorizontalSpacing().multiply(BigDecimal.valueOf(i))));
        }
    }

    // 获得Lifeline的name及alias
    private LifelineName getLifelineName(String text) {
        String lifelineText = text.substring(USDDIConstants.LIFELINE_TITLE_FLAG.length());
        if (lifelineText.isEmpty()) {
            System.err.println("指定的生命线名称为空: " + text);
            return null;
        }

        LifelineName lifelineName = new LifelineName();
        int aliasFlagIndex = lifelineText.indexOf(USDDIConstants.LIFELINE_ALIAS_FLAG);
        if (aliasFlagIndex == -1) {
            // 当前Lifeline的name未指定alias
            lifelineName.setDisplayedName(lifelineText);
            lifelineName.setNameAlias(null);
            return lifelineName;
        }

        // 当前Lifeline的name有指定alias
        String displayedName = lifelineText.substring(0, aliasFlagIndex).trim();
        if (displayedName.isEmpty()) {
            System.err.println("指定的生命线用于展示的名称为空: " + text);
            return null;
        }

        String alias = lifelineText.substring(aliasFlagIndex + USDDIConstants.LIFELINE_ALIAS_FLAG.length()).trim();
        if (alias.isEmpty()) {
            System.err.println("指定的生命线的别名为空: " + text);
            return null;
        }

        lifelineName.setDisplayedName(displayedName);
        lifelineName.setNameAlias(alias);
        return lifelineName;
    }
}
