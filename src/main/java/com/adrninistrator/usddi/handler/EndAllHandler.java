package com.adrninistrator.usddi.handler;

import com.adrninistrator.usddi.conf.ConfStyleInfo;
import com.adrninistrator.usddi.dto.LifelineInfo;
import com.adrninistrator.usddi.dto.MessageInfo;
import com.adrninistrator.usddi.enums.MessageTypeEnum;
import com.adrninistrator.usddi.handler.base.BaseHandler;

import java.util.List;

/**
 * @author adrninistrator
 * @date 2021/9/17
 * @description:
 */
public class EndAllHandler extends BaseHandler {

    public boolean handle() {
        // 完全处理结束时的处理

        List<LifelineInfo> lifelineInfoList = usedVariables.getLifelineInfoList();
        if (lifelineInfoList.isEmpty()) {
            System.err.println("未指定生命线");
            return false;
        }

        // 设置整个区域的宽度，等于最后一个生命线中点X坐标，加上生命线宽度的一半
        LifelineInfo lastLifeline = lifelineInfoList.get(lifelineInfoList.size() - 1);
        usedVariables.setTotalWidth(lastLifeline.getCenterX().add(confPositionInfo.getLifelineBoxWidthHalf()));

        // 设置Lifeline的总高度，等于当前处理到的y坐标加上Message（及与Lifeline之间）垂直间距，减去Lifeline的起始y坐标
        usedVariables.setLifelineHeight(usedVariables.getCurrentY().add(confPositionInfo.getMessageVerticalSpacing()).subtract(usedVariables.getLifelineStartY()));

        ConfStyleInfo confStyleInfo = ConfStyleInfo.getInstance();
        if (confStyleInfo.isMessageAutoSeq()) {
            // 自动为消息添加序号
            addSeq4Message();
        }

        return true;
    }

    // 自动为消息添加序号
    private void addSeq4Message() {
        List<MessageInfo> messageInfoList = usedVariables.getMessageInfoList();
        if (messageInfoList.isEmpty()) {
            return;
        }

        // 获取需要添加序号的消息总数，返回消息不添加
        int messageNum = 0;
        for (MessageInfo messageInfo : messageInfoList) {
            if (messageInfo.getMessageType() != MessageTypeEnum.MTE_RSP) {
                messageNum++;
            }
        }

        String format = "%0" + String.valueOf(messageNum).length() + "d";

        // 为消息添加序号
        int seq = 0;
        for (MessageInfo messageInfo : messageInfoList) {
            if (messageInfo.getMessageType() != MessageTypeEnum.MTE_RSP) {
                seq++;
                String seqStr = String.format(format, seq) + ". ";
                messageInfo.setMessageText(seqStr + messageInfo.getMessageText());
            }
        }
    }
}
