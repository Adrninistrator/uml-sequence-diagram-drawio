package com.adrninistrator.usddi.handler;

import com.adrninistrator.usddi.common.USDDIConstants;
import com.adrninistrator.usddi.dto.LifelineInfo;
import com.adrninistrator.usddi.dto.LifelineName;
import com.adrninistrator.usddi.handler.base.BaseHandler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author adrninistrator
 * @date 2021/9/16
 * @description:
 */
public class LifelineHandler extends BaseHandler {

    // 添加Lifeline
    public boolean addLifeline(String text) {
        // 获得Lifeline的name及alias
        LifelineName lifelineName = getLifelineName(text);
        if (lifelineName == null) {
            return false;
        }

        String displayedName = lifelineName.getDisplayedName();
        String nameAlias = lifelineName.getNameAlias();

        // 判断当前用于展示的name是否已记录
        Map<String, Integer> lifelineDisplayedNameMap = usedVariables.getLifelineDisplayedNameMap();
        if (lifelineDisplayedNameMap.get(displayedName) != null) {
            System.err.println("指定的Lifeline用于展示的name重复: " + displayedName);
            return false;
        }

        // 判断当前用于展示的name是否已记录
        Map<String, Integer> lifelineNameAliasMap = usedVariables.getLifelineNameAliasMap();
        if (nameAlias != null && lifelineNameAliasMap.get(nameAlias) != null) {
            System.err.println("指定的Lifeline的name别名重复: " + nameAlias);
            return false;
        }

        // 记录Lifeline
        List<LifelineInfo> lifelineInfoList = usedVariables.getLifelineInfoList();

        LifelineInfo lifelineInfo = new LifelineInfo();
        lifelineInfo.setDisplayedName(displayedName);

        int currentLifelineSeq = lifelineInfoList.size();

        // 中间点x坐标 = Lifeline方框宽度的1/2 + (当前Lifeline序号-1) * Lifeline中间点的水平间距
        lifelineInfo.setCenterX(confPositionInfo.getLifelineBoxWidthHalf().add(
                confPositionInfo.getLifelineCenterHorizontalSpacing().multiply(BigDecimal.valueOf(currentLifelineSeq))));
        lifelineInfoList.add(lifelineInfo);

        // 记录Lifeline用于展示的name及在List中的序号
        lifelineDisplayedNameMap.put(displayedName, currentLifelineSeq);

        if (nameAlias != null) {
            // 记录Lifeline的name别名及在List中的序号
            lifelineNameAliasMap.put(nameAlias, currentLifelineSeq);
        }

        return true;
    }

    // 获得Lifeline的name及alias
    private LifelineName getLifelineName(String text) {
        String lifelineText = text.substring(USDDIConstants.LIFELINE_TITLE_FLAG.length());
        if (lifelineText.isEmpty()) {
            System.err.println("指定的Lifeline name为空");
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
            System.err.println("指定的Lifeline用于展示的name为空");
            return null;
        }

        String alias = lifelineText.substring(aliasFlagIndex + USDDIConstants.LIFELINE_ALIAS_FLAG.length()).trim();
        if (alias.isEmpty()) {
            System.err.println("指定的Lifeline的name别名为空");
            return null;
        }

        lifelineName.setDisplayedName(displayedName);
        lifelineName.setNameAlias(alias);
        return lifelineName;
    }
}
