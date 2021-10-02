package com.adrninistrator.usddi.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;

/**
 * @author adrninistrator
 * @date 2021/9/24
 * @description:
 */
public class CommonUtil {

    private static ClassLoader classLoader = CommonUtil.class.getClassLoader();

    private static String classpath = CommonUtil.class.getResource("/").getPath();

    public static boolean isStrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static File findFile(String filePath) throws URISyntaxException {
        File file = new File(filePath);
        if (file.exists()) {
            return file;
        }

        URL url = classLoader.getResource(filePath);
        if (url != null) {
            return new File(url.toURI());
        }
        return new File(classpath + filePath);
    }

    public static String currentTime() {
        Calendar calendar = Calendar.getInstance();
        return String.format("%04d%02d%02d-%02d%02d%02d.%03d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));
    }

    private CommonUtil() {
        throw new IllegalStateException("illegal");
    }
}
