package com.adrninistrator.usddi.handler;

import com.adrninistrator.usddi.common.USDDIConstants;
import com.adrninistrator.usddi.dto.DescriptionInfo;
import com.adrninistrator.usddi.handler.base.BaseHandler;
import com.adrninistrator.usddi.util.USDDIUtil;

/**
 * @author adrninistrator
 * @date 2021/10/28
 * @description:
 */
public class DescriptionHandler extends BaseHandler {

    public boolean handleDescription(String text) {
        String descriptionText = text.substring(USDDIConstants.DESCRIPTION_FLAG.length());
        if (descriptionText.isEmpty()) {
            System.err.println("指定的描述为空 " + text);
            return false;
        }

        String descriptionContent;
        String link = null;
        int linkFlagIndex = descriptionText.indexOf(USDDIConstants.LINK_FLAG);
        if (linkFlagIndex != -1) {
            link = descriptionText.substring(linkFlagIndex + USDDIConstants.LINK_FLAG.length());
            descriptionContent = descriptionText.substring(0, linkFlagIndex).trim();
        } else {
            descriptionContent = descriptionText.trim();
        }

        if (descriptionContent.isEmpty()) {
            System.err.println("指定的描述内容为空 " + text);
            return false;
        }

        DescriptionInfo descriptionInfo = usedVariables.getDescriptionInfo();
        descriptionInfo.setUsed(true);
        descriptionInfo.setDescription(descriptionContent);

        // 处理当前处理到的y坐标，加上描述的高度
        usedVariables.addCurrentY(USDDIConstants.DESCRIPTION_HEIGHT);

        if (!USDDIUtil.isStrEmpty(link)) {
            // 未指定链接
            descriptionInfo.setLink(link);
            // 处理当前处理到的y坐标，加上描述与生命线的间距
            usedVariables.addCurrentY(USDDIConstants.DESCRIPTION_WITHOUT_LINK_LIFELINE_VERTICAL_SPACING);
        } else {
            // 有指定链接
            usedVariables.addCurrentY(USDDIConstants.DESCRIPTION_WITH_LINK_LIFELINE_VERTICAL_SPACING);
        }

        // 记录Lifeline的起始y坐标为当前处理到的y坐标
        usedVariables.setLifelineStartY(usedVariables.getCurrentY());

        return true;
    }
}
