package com.adrninistrator.usddi.logger;

import com.adrninistrator.usddi.dto.activation.ActivationInfo;
import com.adrninistrator.usddi.dto.message.MessageInText;
import com.adrninistrator.usddi.dto.message.MessageInfo;
import com.adrninistrator.usddi.util.USDDIUtil;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * @author adrninistrator
 * @date 2022/1/28
 * @description:
 */
public class DebugLogger {

    private static boolean debug = System.getProperty("debug.log") != null;

    private static Writer loggerWriter;

    public static void init() {
        if (!debug) {
            return;
        }

        try {
            loggerWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("USDDI-" + USDDIUtil.currentTime() + ".log")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void beforeExit() {
        if (!debug || loggerWriter == null) {
            return;
        }

        try {
            loggerWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void log(Class clazz, String... data) {
        if (!debug || loggerWriter == null) {
            return;
        }

        StringBuilder logData = new StringBuilder();
        logData.append(clazz.getSimpleName());
        for (String str : data) {
            logData.append("\t").append(str);
        }
        logData.append("\n");

        try {
            loggerWriter.write(logData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logMessageInText(Class clazz, String operate, MessageInText messageInText) {
        log(clazz, operate, getLifelineSeq(messageInText.getStartLifelineSeq()) + "->" +
                getLifelineSeq(messageInText.getEndLifelineSeq()) + ":"
                + messageInText.getMessageText());
    }

    public static void logMessageInfo(Class clazz, String operate, MessageInfo messageInfo) {
        log(clazz, operate, getLifelineSeq(messageInfo.getStartLifelineSeq()) + "->" +
                getLifelineSeq(messageInfo.getEndLifelineSeq()) + ":"
                + messageInfo.getMessageText());
    }

    public static void logActivation(Class clazz, String type, int lifelineSeq, ActivationInfo activationInfo) {
        String topY = activationInfo.getTopY() != null ? activationInfo.getTopY().toPlainString() : "null";
        String bottomY = activationInfo.getBottomY() != null ? activationInfo.getBottomY().toPlainString() : "null";

        log(clazz, type, getLifelineSeq(lifelineSeq), "topY:" + topY, "bottomY:" + bottomY);
    }

    public static String getLifelineSeq(int lifelineSeq) {
        return "[" + (lifelineSeq + 1) + "]";
    }
}
