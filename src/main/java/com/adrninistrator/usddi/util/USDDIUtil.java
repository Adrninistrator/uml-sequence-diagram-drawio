package com.adrninistrator.usddi.util;

import com.adrninistrator.usddi.common.USDDIConstants;
import com.adrninistrator.usddi.conf.ConfStyleInfo;
import com.adrninistrator.usddi.dto.message.MessageFlagIndex;
import com.adrninistrator.usddi.dto.message.MessageInText;
import com.adrninistrator.usddi.dto.message.MessageInfo;
import com.adrninistrator.usddi.dto.variables.UsedVariables;
import com.adrninistrator.usddi.enums.MessageTypeEnum;
import com.adrninistrator.usddi.handler.base.BaseMessageHandler;
import com.adrninistrator.usddi.logger.DebugLogger;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @author adrninistrator
 * @date 2021/9/24
 * @description:
 */
public class USDDIUtil {

    public static final String[] FLAG_ARRAY = new String[]{
            USDDIConstants.MESSAGE_REQ_FLAG, USDDIConstants.MESSAGE_RSP_FLAG, USDDIConstants.MESSAGE_ASYNC_FLAG};

    private static ClassLoader CLASS_LOADER = USDDIUtil.class.getClassLoader();

    private static String CLASS_PATH = USDDIUtil.class.getResource("/").getPath();

    public static boolean isStrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static File findFile(String filePath) throws URISyntaxException {
        File file = new File(filePath);
        if (file.exists()) {
            return file;
        }

        URL url = CLASS_LOADER.getResource(filePath);
        if (url != null) {
            return new File(url.toURI());
        }
        return new File(CLASS_PATH + filePath);
    }

    public static String currentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss.SSS");
        return sdf.format(new Date());
    }

    public static BigDecimal getHalfBigDecimal(BigDecimal bigDecimal) {
        return bigDecimal.divide(BigDecimal.valueOf(2.0D), 2, RoundingMode.HALF_UP);
    }

    public static MessageInfo getMessageInfo(MessageInText messageInText, int partSeq) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setMessageText(messageInText.getMessageText());
        messageInfo.setStartLifelineSeq(messageInText.getStartLifelineSeq());
        messageInfo.setEndLifelineSeq(messageInText.getEndLifelineSeq());
        messageInfo.setLink(messageInText.getLink());
        messageInfo.setPartSeq(partSeq);

        /*
            获得消息的高度：
            文字大小 * 文字行数 + (文字行数 - 1) * 2
         */
        String[] array = messageInfo.getMessageText().split(USDDIConstants.HTML_NEW_LINE);
        long rowNum = array.length;
        messageInfo.setHeight(BigDecimal.valueOf(ConfStyleInfo.getInstance().getTextSizeOfMessage() * rowNum + (rowNum - 1) * 2));

        return messageInfo;
    }

    // 获得Message中的标志
    public static MessageFlagIndex getFlagInMessage(String text) {
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
            System.err.println("当前消息中不存在标志: " + Arrays.toString(FLAG_ARRAY));
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
    public static MessageInText getMessageInText(String text) {
        DebugLogger.log(BaseMessageHandler.class, "parseMessage", text);

        // 获得Message中的标志
        MessageFlagIndex messageFlagIndex = getFlagInMessage(text);
        if (messageFlagIndex == null) {
            return null;
        }

        String startLifelineName = text.substring(0, messageFlagIndex.getIndex()).trim();
        if (startLifelineName.isEmpty()) {
            System.err.println("当前消息的起点生命线名称为空: " + text);
            return null;
        }

        UsedVariables usedVariables = UsedVariables.getInstance();
        Map<String, Integer> lifelineDisplayedNameMap = usedVariables.getLifelineDisplayedNameMap();
        Map<String, Integer> lifelineNameAliasMap = usedVariables.getLifelineNameAliasMap();
        Integer startLifelineSeq = lifelineDisplayedNameMap.get(startLifelineName);
        if (startLifelineSeq == null) {
            startLifelineSeq = lifelineNameAliasMap.get(startLifelineName);
            if (startLifelineSeq == null) {
                System.err.println("当前消息起点不在已指定的生命线名称或别名中: " + text);
                return null;
            }
        }

        int messageTextIndex = text.indexOf(USDDIConstants.MESSAGE_TEXT_FLAG);
        if (messageTextIndex == -1) {
            System.err.println("当前消息未指定文字: " + text);
            return null;
        }

        int linkFlagIndex = text.indexOf(USDDIConstants.LINK_FLAG);
        String link = null;

        String messageText;
        if (linkFlagIndex != -1) {
            // 指定了链接
            messageText = text.substring(messageTextIndex + USDDIConstants.MESSAGE_TEXT_FLAG.length(), linkFlagIndex).trim();
            link = text.substring(linkFlagIndex + USDDIConstants.LINK_FLAG.length()).trim();
        } else {
            // 未指定链接
            messageText = text.substring(messageTextIndex + USDDIConstants.MESSAGE_TEXT_FLAG.length()).trim();
        }
        if (messageText.isEmpty()) {
            System.err.println("当前消息文字为空: " + text);
            return null;
        }

        String endLifelineName = text.substring(messageFlagIndex.getIndex() + messageFlagIndex.getFlag().length(), messageTextIndex).trim();
        if (endLifelineName.isEmpty()) {
            System.err.println("当前消息的终点生命线名称为空: " + text);
            return null;
        }
        Integer endLifelineSeq = lifelineDisplayedNameMap.get(endLifelineName);
        if (endLifelineSeq == null) {
            endLifelineSeq = lifelineNameAliasMap.get(endLifelineName);
            if (endLifelineSeq == null) {
                System.err.println("当前消息终点不在已指定的生命线名称或别名中: " + text);
                return null;
            }
        }

        MessageInText messageInText = new MessageInText();
        if (USDDIConstants.MESSAGE_RSP_FLAG.equals(messageFlagIndex.getFlag())) {
            // 对于返回Message，左边是终点，右边是起点
            messageInText.setStartLifelineSeq(endLifelineSeq);
            messageInText.setEndLifelineSeq(startLifelineSeq);
        } else {
            messageInText.setStartLifelineSeq(startLifelineSeq);
            messageInText.setEndLifelineSeq(endLifelineSeq);
        }
        messageInText.setMessageText(messageText);
        if (USDDIConstants.MESSAGE_REQ_FLAG.equals(messageFlagIndex.getFlag())) {
            messageInText.setMessageType(startLifelineSeq.equals(endLifelineSeq) ? MessageTypeEnum.MTE_SELF : MessageTypeEnum.MTE_REQ);
        } else if (USDDIConstants.MESSAGE_RSP_FLAG.equals(messageFlagIndex.getFlag())) {
            messageInText.setMessageType(MessageTypeEnum.MTE_RSP);
        } else if (USDDIConstants.MESSAGE_ASYNC_FLAG.equals(messageFlagIndex.getFlag())) {
            messageInText.setMessageType(MessageTypeEnum.MTE_ASYNC);
        }

        if (!isStrEmpty(link)) {
            messageInText.setLink(link);
        }

        return messageInText;
    }

    /**
     * 获取上一条消息的下y，需要考虑异步消息
     *
     * @param lastMessageInfo
     * @return
     */
    public static BigDecimal getLastMessageBottomY(MessageInfo lastMessageInfo) {
        BigDecimal lastMessageBottomY = lastMessageInfo.getBottomY();
        if (lastMessageInfo.getMessageType() == MessageTypeEnum.MTE_ASYNC) {
            // 异步消息
            if (lastMessageInfo.getAsyncMessageEndActivationBottomY() != null && lastMessageInfo.getAsyncMessageEndActivationBottomY().compareTo(lastMessageBottomY) > 0) {
                // 若终点对应的激活下y非空，且比异步消息下y值要大时，则使用该终点对应的激活下y
                return lastMessageInfo.getAsyncMessageEndActivationBottomY();
            }
        }

        return lastMessageBottomY;
    }

    private USDDIUtil() {
        throw new IllegalStateException("illegal");
    }
}
