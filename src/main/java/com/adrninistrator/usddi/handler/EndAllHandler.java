package com.adrninistrator.usddi.handler;

import com.adrninistrator.usddi.conf.ConfPositionInfo;
import com.adrninistrator.usddi.conf.ConfStyleInfo;
import com.adrninistrator.usddi.dto.lifeline.LifelineInfo;
import com.adrninistrator.usddi.dto.message.MessageInfo;
import com.adrninistrator.usddi.dto.variables.UsedVariables;
import com.adrninistrator.usddi.enums.MessageTypeEnum;
import com.adrninistrator.usddi.handler.base.BaseHandler;
import com.adrninistrator.usddi.html.HtmlHandler;
import com.adrninistrator.usddi.logger.DebugLogger;
import com.adrninistrator.usddi.util.USDDIUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author adrninistrator
 * @date 2021/9/17
 * @description:
 */
public class EndAllHandler extends BaseHandler {

    public EndAllHandler(UsedVariables usedVariables, ConfPositionInfo confPositionInfo, ConfStyleInfo confStyleInfo, HtmlHandler htmlHandler) {
        super(usedVariables, confPositionInfo, confStyleInfo, htmlHandler);
    }

    public boolean handle() {
        DebugLogger.log(this.getClass(), "end all");
        // 完全处理结束时的处理

        List<LifelineInfo> lifelineInfoList = usedVariables.getLifelineInfoList();
        if (lifelineInfoList.isEmpty()) {
            System.err.println("未指定过生命线");
            return true;
        }

        // 设置整个区域的宽度，等于最后一个生命线中点X坐标，加上生命线宽度的一半
        LifelineInfo lastLifeline = lifelineInfoList.get(lifelineInfoList.size() - 1);
        usedVariables.setTotalWidth(lastLifeline.getCenterX().add(usedVariables.getLifelineBoxActualWidthHalf()));

        // 获取上一条消息
        MessageInfo lastMessageInfo = getLastMessageInfo();
        BigDecimal lastMessageBottomY = USDDIUtil.getLastMessageBottomY(lastMessageInfo);

        /*
            设置Lifeline的总高度，等于最后一条消息的y坐标加上Message（及与Lifeline之间）垂直间距，减去Lifeline的起始y坐标
            加上Message（及与Lifeline之间）垂直间距，是为了使最后一个消息与整个生命线下y之间留出距离
         */
        usedVariables.setLifelineTotalHeight(lastMessageBottomY.add(confPositionInfo.getMessageVerticalSpacing()).subtract(usedVariables.getLifelineStartY()));

        if (confStyleInfo.isMessageAutoSeq()) {
            // 自动为消息添加序号
            addSeq4Message();
        }

        return true;
    }

    // 自动为消息添加序号
    private void addSeq4Message() {
        if (messageInfoList.isEmpty()) {
            return;
        }

        // 当存在超过一个部分时，为每个部分指定序号
        boolean usePartSeq = usedVariables.getCurrentPartSeq() > 1;
        String partSeqFormat = null;
        if (usePartSeq) {
            partSeqFormat = "%0" + String.valueOf(usedVariables.getCurrentPartSeq()).length() + "d";
        }

        // 逐个处理各个部分的序号
        for (int partSeq = 0; partSeq <= usedVariables.getCurrentPartSeq(); partSeq++) {
            // 获取需要添加序号的消息总数，返回消息不添加
            int messageNum = 0;
            for (MessageInfo messageInfo : messageInfoList) {
                if (messageInfo.getPartSeq() == partSeq && messageInfo.getMessageType() != MessageTypeEnum.MTE_RSP) {
                    messageNum++;
                }
            }

            String format = "%0" + String.valueOf(messageNum).length() + "d";

            // 为消息添加序号
            int seq = 0;
            for (MessageInfo messageInfo : messageInfoList) {
                if (messageInfo.getPartSeq() == partSeq && messageInfo.getMessageType() != MessageTypeEnum.MTE_RSP) {
                    seq++;
                    String partSeqStr = "";
                    if (usePartSeq) {
                        // 当前部分的序号，从0开始，需要加1
                        partSeqStr = String.format(partSeqFormat, partSeq + 1) + ".";
                    }

                    // 当前部分内某个消息的序号
                    String seqStr = String.format(format, seq) + ". ";
                    messageInfo.setMessageText(partSeqStr + seqStr + messageInfo.getMessageText());
                }
            }
        }
    }
}
