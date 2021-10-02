package com.adrninistrator.usddi.handler;

import com.adrninistrator.usddi.handler.base.BaseHandler;

/**
 * @author adrninistrator
 * @date 2021/9/17
 * @description:
 */
public class EndAllHandler extends BaseHandler {

    public void handle() {
        // 完全处理结束时，将Lifeline的最下端y坐标加上Message（及与Lifeline之间）垂直间距
        usedVariables.setTotalHeight(usedVariables.getCurrentY().add(confPositionInfo.getMessageVerticalSpacing()));
    }
}
