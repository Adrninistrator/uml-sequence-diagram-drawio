package com.adrninistrator.usddi.handler.base;

import com.adrninistrator.usddi.common.Constants;
import com.adrninistrator.usddi.dto.*;
import com.adrninistrator.usddi.enums.MessageTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author adrninistrator
 * @date 2021/9/16
 * @description:
 */
public abstract class BaseMessageHandler extends BaseHandler {

    public static final String[] FLAG_ARRAY = new String[]{
            Constants.MESSAGE_REQ_FLAG, Constants.MESSAGE_RSP_FLAG, Constants.MESSAGE_ASYNC_FLAG};

    // 获得Message中的标志
    private static MessageFlagIndex getFlagInMessage(String text) {
        String usedFlag = null;
        int minIndex = -1;
        for (String flag : FLAG_ARRAY) {
            int index = text.indexOf(flag);
            if (index == -1) {
                continue;
            }
            if (minIndex == -1 || index < minIndex) {
                minIndex = index;
                usedFlag = flag;
            }
        }

        if (usedFlag == null) {
            System.err.println("当前Message中不存在标志");
            return null;
        }

        MessageFlagIndex messageFlagIndex = new MessageFlagIndex();
        messageFlagIndex.setIndex(minIndex);
        messageFlagIndex.setFlag(usedFlag);

        return messageFlagIndex;
    }

    /**
     * 解析当前Message数据
     *
     * @param text
     * @return
     */
    public static MessageInText parseMessage(String text) {
        // 获得Message中的标志
        MessageFlagIndex messageFlagIndex = BaseMessageHandler.getFlagInMessage(text);
        if (messageFlagIndex == null) {
            return null;
        }

        String startLifelineName = text.substring(0, messageFlagIndex.getIndex()).trim();
        if (startLifelineName.isEmpty()) {
            System.err.println("当前Message的起点Lifeline name为空");
            return null;
        }

        UsedVariables usedVariables = UsedVariables.getInstance();
        Map<String, Integer> lifelineDisplayedNameMap = usedVariables.getLifelineDisplayedNameMap();
        Map<String, Integer> lifelineNameAliasMap = usedVariables.getLifelineNameAliasMap();
        Integer startLifelineSeq = lifelineDisplayedNameMap.get(startLifelineName);
        if (startLifelineSeq == null) {
            startLifelineSeq = lifelineNameAliasMap.get(startLifelineName);
            if (startLifelineSeq == null) {
                System.err.println("当前Message起点不在已指定的Lifeline name中");
                return null;
            }
        }

        int messageTextIndex = text.indexOf(Constants.MESSAGE_TEXT_FLAG);
        if (messageTextIndex == -1) {
            System.err.println("当前Message未指定文字");
            return null;
        }

        String messageText = text.substring(messageTextIndex + Constants.MESSAGE_TEXT_FLAG.length()).trim();
        if (messageText.isEmpty()) {
            System.err.println("当前Message文字为空");
            return null;
        }

        String endLifelineName = text.substring(messageFlagIndex.getIndex() + messageFlagIndex.getFlag().length(), messageTextIndex).trim();
        if (endLifelineName.isEmpty()) {
            System.err.println("当前Message的终点Lifeline name为空");
            return null;
        }
        Integer endLifelineSeq = lifelineDisplayedNameMap.get(endLifelineName);
        if (endLifelineSeq == null) {
            endLifelineSeq = lifelineNameAliasMap.get(endLifelineName);
            if (endLifelineSeq == null) {
                System.err.println("当前Message终点不在已指定的Lifeline name中");
                return null;
            }
        }

        MessageInText messageInText = new MessageInText();
        if (Constants.MESSAGE_RSP_FLAG.equals(messageFlagIndex.getFlag())) {
            // 对于返回Message，左边是终点，右边是起点
            messageInText.setStartLifelineSeq(endLifelineSeq);
            messageInText.setEndLifelineSeq(startLifelineSeq);
        } else {
            messageInText.setStartLifelineSeq(startLifelineSeq);
            messageInText.setEndLifelineSeq(endLifelineSeq);
        }
        messageInText.setMessageText(messageText);
        if (Constants.MESSAGE_REQ_FLAG.equals(messageFlagIndex.getFlag())) {
            messageInText.setMessageType(startLifelineSeq.equals(endLifelineSeq) ? MessageTypeEnum.MTE_SELF : MessageTypeEnum.MTE_REQ);
        } else if (Constants.MESSAGE_RSP_FLAG.equals(messageFlagIndex.getFlag())) {
            messageInText.setMessageType(MessageTypeEnum.MTE_RSP);
        } else if (Constants.MESSAGE_ASYNC_FLAG.equals(messageFlagIndex.getFlag())) {
            messageInText.setMessageType(MessageTypeEnum.MTE_ASYNC);
        }

        return messageInText;
    }

    // 对Message进行处理
    public abstract boolean handleMessage(MessageInText messageInText);

    /**
     * 检查栈顶元素
     *
     * @param messageInText
     * @return true: 检查通过，false: 检查不通过
     */
    protected boolean checkStackTop(MessageInText messageInText) {
        if (!messageStack.isEmpty()) {
            // 当栈非空时，当前Message只能以栈顶的终点Lifeline作为起点
            MessageInStack topMessage = messageStack.peek();
            if (!messageInText.getStartLifelineSeq().equals(topMessage.getEndLifelineSeq())) {
                System.err.println("当前请求的起点与上次请求的终点不同: " + messageInText.getStartLifelineSeq() +
                        Constants.MESSAGE_REQ_FLAG + topMessage.getEndLifelineSeq());
                return false;
            }
        }
        return true;
    }

    /**
     * 检查Message起点
     *
     * @param messageInText
     * @return true: 检查通过，false: 检查不通过
     */
    protected boolean checkMessageStart(MessageInText messageInText) {
        if (messageStack.isEmpty()) {
            // 当栈为空时，检查当前Message是否以最初的起点Lifeline为起点
            if (usedVariables.getFirstStartLifelineSeq() == null) {
                // 最初的起点Lifeline为空时，进行设置
                usedVariables.setFirstStartLifelineSeq(messageInText.getStartLifelineSeq());
                return true;
            } else {
                // 最初的起点Lifeline非空时，检查是否与当前Message起点相同
                if (!usedVariables.getFirstStartLifelineSeq().equals(messageInText.getStartLifelineSeq())) {
                    System.err.println("需要以最初的起点Lifeline作为当前Message的起点");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 处理Message位置
     *
     * @param messageInfo
     * @param messageInText
     */
    protected void setMessagePosition(MessageInfo messageInfo, MessageInText messageInText) {
        // y坐标为当前处理到的y坐标
        messageInfo.setStartY(usedVariables.getCurrentY());
        messageInfo.setEndY(usedVariables.getCurrentY());

        List<LifelineInfo> lifelineInfoList = usedVariables.getLifelineInfoList();
        // 获取起点与终点Lifeline
        LifelineInfo startLifelineInfo = lifelineInfoList.get(messageInText.getStartLifelineSeq());
        LifelineInfo endLifelineInfo = lifelineInfoList.get(messageInText.getEndLifelineSeq());

        if (messageInText.getStartLifelineSeq() < messageInText.getEndLifelineSeq()) {
            // 起点在终点的左边
            // 起点x坐标为起点Lifeline中间点的x坐标加上配置中指定的Activation宽度的一半
            messageInfo.setStartX(startLifelineInfo.getCenterX().add(confPositionInfo.getActivationWidthHalf()));
            // 终点x坐标为终点Lifeline中间点的x坐标减去配置中指定的Activation宽度的一半
            messageInfo.setEndX(endLifelineInfo.getCenterX().subtract(confPositionInfo.getActivationWidthHalf()));
        } else {
            // 起点在终点的右边
            // 起点x坐标为起点Lifeline中间点的x坐标减去配置中指定的Activation宽度的一半
            messageInfo.setStartX(startLifelineInfo.getCenterX().subtract(confPositionInfo.getActivationWidthHalf()));
            // 终点x坐标为终点Lifeline中间点的x坐标加上配置中指定的Activation宽度的一半
            messageInfo.setEndX(endLifelineInfo.getCenterX().add(confPositionInfo.getActivationWidthHalf()));
        }
    }

    /**
     * 尝试为起点Lifeline增加Activation
     *
     * @param messageInText
     */
    protected void tryAddActivation4StartLifeline(MessageInText messageInText) {
        Map<Integer, List<ActivationInfo>> activationMap = usedVariables.getActivationMap();

        // 在记录Activation的Map中，起点Lifeline的Activation List中，判断最后一个Activation
        List<ActivationInfo> startActivationInfoList = activationMap.computeIfAbsent(messageInText.getStartLifelineSeq(), k -> new ArrayList<>());
        if (startActivationInfoList.isEmpty() || startActivationInfoList.get(startActivationInfoList.size() - 1).getEndY() != null) {
            // 若Activation不存在，或者最后一个Activation的结束y坐标已设置，则增加一个Activation，起始y坐标为当前处理到的y坐标
            ActivationInfo startActivationInfo = new ActivationInfo();
            startActivationInfoList.add(startActivationInfo);
            startActivationInfo.setStartY(usedVariables.getCurrentY());
            startActivationInfo.setEndY(null);
        }
    }
}
