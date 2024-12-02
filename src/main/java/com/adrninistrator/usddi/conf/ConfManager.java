package com.adrninistrator.usddi.conf;

import com.adrninistrator.usddi.common.USDDIConstants;
import com.adrninistrator.usddi.exceptions.ConfException;
import com.adrninistrator.usddi.util.USDDIUtil;

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

    public static final Pattern PATTERN_COLOR = Pattern.compile("#[A-Fa-f0-9]{6}");

    private final ConfPositionInfo confPositionInfo = new ConfPositionInfo();
    private final ConfStyleInfo confStyleInfo = new ConfStyleInfo();

    public boolean handlePositionConf() {
        String configFilePath = USDDIConstants.CONF_DIR + File.separator + USDDIConstants.CONF_FILE_POSITION;

        try (Reader reader = new InputStreamReader(new FileInputStream(USDDIUtil.findFile(configFilePath)), StandardCharsets.UTF_8)) {
            Properties properties = new Properties();
            properties.load(reader);

            BigDecimal lifelineCenterHorizontalSpacing = getBigDecimalValueAtLeastZero(properties, USDDIConstants.KEY_LIFELINE_CENTER_HORIZONTAL_SPACING, configFilePath, false);
            BigDecimal messageVerticalSpacing = getBigDecimalValueAtLeastZero(properties, USDDIConstants.KEY_MESSAGE_VERTICAL_SPACING, configFilePath, false);
            BigDecimal selfCallHorizontalWidth = getBigDecimalValueAtLeastZero(properties, USDDIConstants.KEY_SELF_CALL_HORIZONTAL_WIDTH, configFilePath, false);
            BigDecimal activationWidth = getBigDecimalValueAtLeastZero(properties, USDDIConstants.KEY_ACTIVATION_WIDTH, configFilePath, false);
            BigDecimal partsExtraVerticalSpacing = getBigDecimalValueAtLeastZero(properties, USDDIConstants.KEY_PARTS_EXTRA_VERTICAL_SPACING, configFilePath, true);

            confPositionInfo.setLifelineCenterHorizontalSpacing(lifelineCenterHorizontalSpacing);
            confPositionInfo.setMessageVerticalSpacing(messageVerticalSpacing);
            confPositionInfo.setSelfCallHorizontalWidth(selfCallHorizontalWidth);
            confPositionInfo.setActivationWidth(activationWidth);
            confPositionInfo.setActivationWidthHalf(USDDIUtil.getHalfBigDecimal(activationWidth));
            confPositionInfo.setPartsExtraVerticalSpacing(partsExtraVerticalSpacing != null ? partsExtraVerticalSpacing : BigDecimal.ZERO);

            return true;
        } catch (ConfException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean handleStyleConf() {
        String configFilePath = USDDIConstants.CONF_DIR + File.separator + USDDIConstants.CONF_FILE_STYLE;

        try (Reader reader = new InputStreamReader(new FileInputStream(USDDIUtil.findFile(configFilePath)), StandardCharsets.UTF_8)) {
            Properties properties = new Properties();
            properties.load(reader);

            String strMessageAutoSeq = getStrValue(properties, USDDIConstants.KEY_MESSAGE_AUTO_SEQ, configFilePath, true);
            BigDecimal lineWidthOfLifeline = getBigDecimalValueAtLeastZero(properties, USDDIConstants.KEY_LINE_WIDTH_OF_LIFELINE, configFilePath, true);
            BigDecimal lineWidthOfActivation = getBigDecimalValueAtLeastZero(properties, USDDIConstants.KEY_LINE_WIDTH_OF_ACTIVATION, configFilePath, true);
            BigDecimal lineWidthOfMessage = getBigDecimalValueAtLeastZero(properties, USDDIConstants.KEY_LINE_WIDTH_OF_MESSAGE, configFilePath, true);
            String lineColorOfLifeline = getColor(properties, USDDIConstants.KEY_LINE_COLOR_OF_LIFELINE, configFilePath, true);
            String lineColorOfActivation = getColor(properties, USDDIConstants.KEY_LINE_COLOR_OF_ACTIVATION, configFilePath, true);
            String lineColorOfMessage = getColor(properties, USDDIConstants.KEY_LINE_COLOR_OF_MESSAGE, configFilePath, true);
            String boxColorOfLifeline = getColor(properties, USDDIConstants.KEY_BOX_COLOR_OF_LIFELINE, configFilePath, true);
            String boxColorOfActivation = getColor(properties, USDDIConstants.KEY_BOX_COLOR_OF_ACTIVATION, configFilePath, true);
            String textFontOfLifeline = getStrValue(properties, USDDIConstants.KEY_TEXT_FONT_OF_LIFELINE, configFilePath, true);
            String textFontOfMessage = getStrValue(properties, USDDIConstants.KEY_TEXT_FONT_OF_MESSAGE, configFilePath, true);
            Integer textSizeOfLifeline = getIntegerValue(properties, USDDIConstants.KEY_TEXT_SIZE_OF_LIFELINE, configFilePath, true, USDDIConstants.ALLOWED_MIN_FONT_SIZE);
            Integer textSizeOfMessage = getIntegerValue(properties, USDDIConstants.KEY_TEXT_SIZE_OF_MESSAGE, configFilePath, true, USDDIConstants.ALLOWED_MIN_FONT_SIZE);
            String textColorOfLifeline = getColor(properties, USDDIConstants.KEY_TEXT_COLOR_OF_LIFELINE, configFilePath, true);
            String textColorOfMessage = getColor(properties, USDDIConstants.KEY_TEXT_COLOR_OF_MESSAGE, configFilePath, true);

            boolean messageAutoSeq = !Boolean.FALSE.toString().equalsIgnoreCase(strMessageAutoSeq);
            confStyleInfo.setMessageAutoSeq(messageAutoSeq);
            confStyleInfo.setLineWidthOfLifeline(lineWidthOfLifeline);
            confStyleInfo.setLineWidthOfActivation(lineWidthOfActivation);
            confStyleInfo.setLineWidthOfMessage(lineWidthOfMessage);
            confStyleInfo.setLineColorOfLifeline(lineColorOfLifeline);
            confStyleInfo.setLineColorOfActivation(lineColorOfActivation);
            confStyleInfo.setLineColorOfMessage(lineColorOfMessage);
            confStyleInfo.setBoxColorOfLifeline(boxColorOfLifeline);
            confStyleInfo.setBoxColorOfActivation(boxColorOfActivation);
            confStyleInfo.setTextFontOfLifeline(textFontOfLifeline != null ? textFontOfLifeline : USDDIConstants.DEFAULT_FONT_NAME);
            confStyleInfo.setTextFontOfMessage(textFontOfMessage != null ? textFontOfMessage : USDDIConstants.DEFAULT_FONT_NAME);
            confStyleInfo.setTextSizeOfLifeline(textSizeOfLifeline != null ? textSizeOfLifeline : USDDIConstants.DEFAULT_FONT_SIZE);
            confStyleInfo.setTextSizeOfMessage(textSizeOfMessage != null ? textSizeOfMessage : USDDIConstants.DEFAULT_FONT_SIZE);
            confStyleInfo.setTextColorOfLifeline(textColorOfLifeline != null ? textColorOfLifeline : USDDIConstants.DEFAULT_FONT_COLOR);
            confStyleInfo.setTextColorOfMessage(textColorOfMessage != null ? textColorOfMessage : USDDIConstants.DEFAULT_FONT_COLOR);

            return true;
        } catch (ConfException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断配置是否为空
     *
     * @param value
     * @param key
     * @param configFilePath
     * @param allowEmpty
     * @return true: 配置为空 false: 配置非空
     * @throws ConfException
     */
    private boolean checkBlank(String value, String key, String configFilePath, boolean allowEmpty) throws ConfException {
        if (USDDIUtil.isStrEmpty(value)) {
            if (!allowEmpty) {
                throw new ConfException("配置文件中未指定参数: " + configFilePath + " " + key);
            }
            return true;
        }
        return false;
    }

    public BigDecimal getBigDecimalValueAtLeastZero(Properties properties, String key, String configFilePath, boolean allowEmpty) throws ConfException {
        return getBigDecimalValue(properties, key, configFilePath, allowEmpty, BigDecimal.ZERO);
    }

    public BigDecimal getBigDecimalValue(Properties properties, String key, String configFilePath, boolean allowEmpty, BigDecimal allowedMinValue) throws ConfException {
        String strValue = properties.getProperty(key);
        if (checkBlank(strValue, key, configFilePath, allowEmpty)) {
            return null;
        }

        try {
            BigDecimal b = new BigDecimal(strValue);
            if (b.compareTo(allowedMinValue) <= 0) {
                throw new ConfException("配置文件 " + configFilePath + " 参数 " + key + " 值 " + strValue + " 应大于等于 " + allowedMinValue);
            }
            return b;
        } catch (Exception e) {
            throw new ConfException("配置文件中的参数不是合法的数字: " + configFilePath + " " + key + " " + strValue);
        }
    }

    public String getColor(Properties properties, String key, String configFilePath, boolean allowEmpty) throws ConfException {
        String strValue = properties.getProperty(key);
        if (checkBlank(strValue, key, configFilePath, allowEmpty)) {
            return null;
        }

        if (!PATTERN_COLOR.matcher(strValue).matches()) {
            throw new ConfException("配置文件中的颜色参数非法，应为“#xxxxxx”的形式: " + configFilePath + " " + key + " " + strValue);
        }
        return strValue;
    }

    public String getStrValue(Properties properties, String key, String configFilePath, boolean allowEmpty) throws ConfException {
        String strValue = properties.getProperty(key);
        checkBlank(strValue, key, configFilePath, allowEmpty);
        return strValue;
    }

    public Integer getIntegerValue(Properties properties, String key, String configFilePath, boolean allowEmpty, int allowedMinValue) throws ConfException {
        String strValue = properties.getProperty(key);
        if (checkBlank(strValue, key, configFilePath, allowEmpty)) {
            return null;
        }

        try {
            Integer i = new Integer(strValue);
            if (i < allowedMinValue) {
                throw new ConfException("配置文件 " + configFilePath + " 参数 " + key + " 值 " + strValue + " 应大于等于 " + allowedMinValue);
            }
            return i;
        } catch (Exception e) {
            throw new ConfException("配置文件中的参数不是合法的整数: " + configFilePath + " " + key + " " + strValue);
        }
    }

    public ConfPositionInfo getConfPositionInfo() {
        return confPositionInfo;
    }

    public ConfStyleInfo getConfStyleInfo() {
        return confStyleInfo;
    }
}
