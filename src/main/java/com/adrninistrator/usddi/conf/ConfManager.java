package com.adrninistrator.usddi.conf;

import com.adrninistrator.usddi.common.Constants;
import com.adrninistrator.usddi.util.CommonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author adrninistrator
 * @date 2021/9/15
 * @description:
 */
public class ConfManager {

    private static ConfManager instance = new ConfManager();

    public static final Pattern PATTERN_COLOR = Pattern.compile("#[A-Fa-f0-9]{6}");

    public static ConfManager getInstance() {
        return instance;
    }

    public boolean handlePositionConf() {
        String configFilePath = Constants.CONF_DIR + File.separator + Constants.CONF_FILE_POSITION;

        try (Reader reader = new InputStreamReader(new FileInputStream(CommonUtil.findFile(configFilePath)), StandardCharsets.UTF_8)) {
            Properties properties = new Properties();
            properties.load(reader);

            BigDecimal lifelineCenterHorizontalSpacing = getBigDecimalValue(properties, Constants.KEY_LIFELINE_CENTER_HORIZONTAL_SPACING, configFilePath, false);
            if (lifelineCenterHorizontalSpacing == null) {
                return false;
            }

            BigDecimal lifelineBoxWidth = getBigDecimalValue(properties, Constants.KEY_LIFELINE_BOX_WIDTH, configFilePath, false);
            if (lifelineBoxWidth == null) {
                return false;
            }

            BigDecimal lifelineBoxHeight = getBigDecimalValue(properties, Constants.KEY_LIFELINE_BOX_HEIGHT, configFilePath, false);
            if (lifelineBoxHeight == null) {
                return false;
            }

            BigDecimal messageVerticalSpacing = getBigDecimalValue(properties, Constants.KEY_MESSAGE_VERTICAL_SPACING, configFilePath, false);
            if (messageVerticalSpacing == null) {
                return false;
            }

            BigDecimal rspMessageVerticalSpacing = getBigDecimalValue(properties, Constants.KEY_RSP_MESSAGE_VERTICAL_SPACING, configFilePath, false);
            if (rspMessageVerticalSpacing == null) {
                return false;
            }

            BigDecimal selfCallHorizontalWidth = getBigDecimalValue(properties, Constants.KEY_SELF_CALL_HORIZONTAL_WIDTH, configFilePath, false);
            if (selfCallHorizontalWidth == null) {
                return false;
            }

            BigDecimal activationWidth = getBigDecimalValue(properties, Constants.KEY_ACTIVATION_WIDTH, configFilePath, false);
            if (activationWidth == null) {
                return false;
            }

            BigDecimal partsExtraVerticalSpacing = getBigDecimalValue(properties, Constants.KEY_PARTS_EXTRA_VERTICAL_SPACING, configFilePath, true);

            ConfPositionInfo confPositionInfo = ConfPositionInfo.getInstance();
            confPositionInfo.setLifelineCenterHorizontalSpacing(lifelineCenterHorizontalSpacing);
            confPositionInfo.setLifelineBoxWidth(lifelineBoxWidth);
            confPositionInfo.setLifelineBoxWidthHalf(lifelineBoxWidth.divide(BigDecimal.valueOf(2)));
            confPositionInfo.setLifelineBoxHeight(lifelineBoxHeight);
            confPositionInfo.setMessageVerticalSpacing(messageVerticalSpacing);
            confPositionInfo.setMessageVerticalSpacingHalf(messageVerticalSpacing.divide(BigDecimal.valueOf(2)));
            confPositionInfo.setRspMessageVerticalSpacing(rspMessageVerticalSpacing);
            confPositionInfo.setSelfCallHorizontalWidth(selfCallHorizontalWidth);
            confPositionInfo.setActivationWidth(activationWidth);
            confPositionInfo.setActivationWidthHalf(activationWidth.divide(BigDecimal.valueOf(2)));
            confPositionInfo.setPartsExtraVerticalSpacing(partsExtraVerticalSpacing);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean handleStyleConf() {
        String configFilePath = Constants.CONF_DIR + File.separator + Constants.CONF_FILE_STYLE;

        try (Reader reader = new InputStreamReader(new FileInputStream(CommonUtil.findFile(configFilePath)), StandardCharsets.UTF_8)) {
            Properties properties = new Properties();
            properties.load(reader);

            BigDecimal lineWidthOfLifeline = getBigDecimalValue(properties, Constants.KEY_LINE_WIDTH_OF_LIFELINE, configFilePath, true);
            BigDecimal lineWidthOfActivation = getBigDecimalValue(properties, Constants.KEY_LINE_WIDTH_OF_ACTIVATION, configFilePath, true);
            BigDecimal lineWidthOfMessage = getBigDecimalValue(properties, Constants.KEY_LINE_WIDTH_OF_MESSAGE, configFilePath, true);
            String lineColorOfLifeline = getColor(properties, Constants.KEY_LINE_COLOR_OF_LIFELINE, configFilePath, true);
            String lineColorOfActivation = getColor(properties, Constants.KEY_LINE_COLOR_OF_ACTIVATION, configFilePath, true);
            String lineColorOfMessage = getColor(properties, Constants.KEY_LINE_COLOR_OF_MESSAGE, configFilePath, true);
            String boxColorOfLifeline = getColor(properties, Constants.KEY_BOX_COLOR_OF_LIFELINE, configFilePath, true);
            String boxColorOfActivation = getColor(properties, Constants.KEY_BOX_COLOR_OF_ACTIVATION, configFilePath, true);
            String textFontOfLifeline = getStrValue(properties, Constants.KEY_TEXT_FONT_OF_LIFELINE, configFilePath, true);
            String textFontOfMessage = getStrValue(properties, Constants.KEY_TEXT_FONT_OF_MESSAGE, configFilePath, true);
            Integer textSizeOfLifeline = getIntegerValue(properties, Constants.KEY_TEXT_SIZE_OF_LIFELINE, configFilePath, true);
            Integer textSizeOfMessage = getIntegerValue(properties, Constants.KEY_TEXT_SIZE_OF_MESSAGE, configFilePath, true);
            String textColorOfLifeline = getColor(properties, Constants.KEY_TEXT_COLOR_OF_LIFELINE, configFilePath, true);
            String textColorOfMessage = getColor(properties, Constants.KEY_TEXT_COLOR_OF_MESSAGE, configFilePath, true);

            ConfStyleInfo confStyleInfo = ConfStyleInfo.getInstance();
            confStyleInfo.setLineWidthOfLifeline(lineWidthOfLifeline);
            confStyleInfo.setLineWidthOfActivation(lineWidthOfActivation);
            confStyleInfo.setLineWidthOfMessage(lineWidthOfMessage);
            confStyleInfo.setLineColorOfLifeline(lineColorOfLifeline);
            confStyleInfo.setLineColorOfActivation(lineColorOfActivation);
            confStyleInfo.setLineColorOfMessage(lineColorOfMessage);
            confStyleInfo.setBoxColorOfLifeline(boxColorOfLifeline);
            confStyleInfo.setBoxColorOfActivation(boxColorOfActivation);
            confStyleInfo.setTextFontOfLifeline(textFontOfLifeline);
            confStyleInfo.setTextFontOfMessage(textFontOfMessage);
            confStyleInfo.setTextSizeOfLifeline(textSizeOfLifeline);
            confStyleInfo.setTextSizeOfMessage(textSizeOfMessage);
            confStyleInfo.setTextColorOfLifeline(textColorOfLifeline);
            confStyleInfo.setTextColorOfMessage(textColorOfMessage);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkBlank(String value, String key, String configFilePath, boolean allowEmpty) {
        if (CommonUtil.isStrEmpty(value)) {
            if (!allowEmpty) {
                System.err.println("配置文件中未指定参数 " + configFilePath + " " + key);
            }
            return true;
        }
        return false;
    }

    public BigDecimal getBigDecimalValue(Properties properties, String key, String configFilePath, boolean allowEmpty) {
        String strValue = properties.getProperty(key);
        if (checkBlank(strValue, key, configFilePath, allowEmpty)) {
            return null;
        }

        try {
            BigDecimal b = new BigDecimal(strValue);
            if (b.compareTo(BigDecimal.ZERO) <= 0) {
                System.err.println("配置文件中的参数应大于0 " + configFilePath + " " + key + " " + strValue);
                return null;
            }

            return b;
        } catch (Exception e) {
            System.err.println("配置文件中的参数不是合法的数字 " + configFilePath + " " + key + " " + strValue);
            return null;
        }
    }

    public String getColor(Properties properties, String key, String configFilePath, boolean allowEmpty) {
        String strValue = properties.getProperty(key);
        if (checkBlank(strValue, key, configFilePath, allowEmpty)) {
            return null;
        }

        if (!PATTERN_COLOR.matcher(strValue).matches()) {
            System.err.println("配置文件中的颜色参数非法，应为“#xxxxxx”的形式 " + configFilePath + " " + key + " " + strValue);
            return null;
        }
        return strValue;
    }

    public String getStrValue(Properties properties, String key, String configFilePath, boolean allowEmpty) {
        String strValue = properties.getProperty(key);
        if (checkBlank(strValue, key, configFilePath, allowEmpty)) {
            return null;
        }
        return strValue;
    }

    public Integer getIntegerValue(Properties properties, String key, String configFilePath, boolean allowEmpty) {
        String strValue = properties.getProperty(key);
        if (checkBlank(strValue, key, configFilePath, allowEmpty)) {
            return null;
        }

        try {
            Integer i = new Integer(strValue);
            if (i <= 0) {
                System.err.println("配置文件中的参数应大于0 " + configFilePath + " " + key + " " + strValue);
                return null;
            }

            return i;
        } catch (Exception e) {
            System.err.println("配置文件中的参数不是合法的整数 " + configFilePath + " " + key + " " + strValue);
            return null;
        }
    }

}
