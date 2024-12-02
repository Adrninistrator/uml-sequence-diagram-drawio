package com.adrninistrator.usddi.logger;

import com.adrninistrator.usddi.dto.activation.ActivationInfo;
import com.adrninistrator.usddi.dto.message.MessageInText;
import com.adrninistrator.usddi.dto.message.MessageInfo;
import com.adrninistrator.usddi.util.USDDIUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * @author adrninistrator
 * @date 2022/1/28
 * @description:
 */
public class DebugLogger {

    public static final String LOG_DIR = "log";

    public static final String DEBUG_FLAG = "debug.log";

    private static final boolean DEBUG = System.getProperty(DEBUG_FLAG) != null;

    private static Writer loggerWriter;

    private static boolean exited = false;

    private static int logTimes = 0;

    static {
        if (DEBUG) {
            try {
                new File(LOG_DIR).mkdirs();
                loggerWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(LOG_DIR + "/USDDI-" + USDDIUtil.currentTime() + ".log")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Runtime.getRuntime().addShutdownHook(new Thread(DebugLogger::beforeExit));
        }
    }

    public static void beforeExit() {
        if (!DEBUG || loggerWriter == null || exited) {
            return;
        }

        exited = true;

        try {
            loggerWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void emptyLine() {
        if (logTimes == 0) {
            return;
        }
        log(null);
    }

    public static void log(Class<?> clazz, Object... data) {
        if (!DEBUG || loggerWriter == null) {
            return;
        }

        logTimes++;
        StringBuilder logData = new StringBuilder();
        if (clazz != null) {
            logData.append(clazz.getSimpleName());
        }
        if (data != null) {
            for (Object obj : data) {
                logData.append(" ").append(obj);
            }
        }
        logData.append("\n");

        try {
            loggerWriter.write(logData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logMessageInText(Class<?> clazz, String operate, MessageInText messageInText) {
        log(clazz, operate, getLifelineSeq(messageInText.getStartLifelineSeq()) + "->" +
                getLifelineSeq(messageInText.getEndLifelineSeq()) + ":"
                + messageInText.getMessageText());
    }

    public static void logMessageInfo(Class<?> clazz, String operate, MessageInfo messageInfo) {
        log(clazz, operate, getLifelineSeq(messageInfo.getStartLifelineSeq()) + "->" +
                getLifelineSeq(messageInfo.getEndLifelineSeq()) + ":"
                + messageInfo.getMessageText());
    }

    public static void logActivation(Class<?> clazz, String type, int lifelineSeq, ActivationInfo activationInfo) {
        String topY = activationInfo.getTopY() != null ? activationInfo.getTopY().toPlainString() : "null";
        String bottomY = activationInfo.getBottomY() != null ? activationInfo.getBottomY().toPlainString() : "null";

        log(clazz, type, getLifelineSeq(lifelineSeq), "topY:" + topY, "bottomY:" + bottomY);
    }

    public static String getLifelineSeq(int lifelineSeq) {
        return "[" + (lifelineSeq + 1) + "]";
    }

    private DebugLogger() {
        throw new IllegalStateException("illegal");
    }
}
