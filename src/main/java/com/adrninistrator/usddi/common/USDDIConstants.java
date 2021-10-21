package com.adrninistrator.usddi.common;

/**
 * @author adrninistrator
 * @date 2021/9/15
 * @description:
 */
public class USDDIConstants {

    public static final String COMMENT_FLAG = "#";

    public static final String LIFELINE_TITLE_FLAG = "@";

    public static final String LIFELINE_ALIAS_FLAG = " as ";

    public static final String MESSAGE_REQ_FLAG = "=>";

    public static final String MESSAGE_RSP_FLAG = "<=";

    public static final String MESSAGE_ASYNC_FLAG = "->";

    public static final String MESSAGE_TEXT_FLAG = ":";

    public static final String CONF_DIR = "~usddi_conf";

    public static final String CONF_FILE_POSITION = "position.properties";

    public static final String CONF_FILE_STYLE = "style.properties";

    public static final String NEW_LINE = "\n";

    public static final String EXT_DRAWIO = ".drawio";

    public static final String HTML_NEW_LINE = "<br>";

    // Lifeline中间点的水平间距
    public static final String KEY_LIFELINE_CENTER_HORIZONTAL_SPACING = "lifeline.center.horizontal.spacing";
    // Lifeline的方框宽度
    public static final String KEY_LIFELINE_BOX_WIDTH = "lifeline.box.width";
    // Lifeline的方框高度
    public static final String KEY_LIFELINE_BOX_HEIGHT = "lifeline.box.height";
    // Message（及与Lifeline之间）垂直间距
    public static final String KEY_MESSAGE_VERTICAL_SPACING = "message.vertical.spacing";
    // Message请求及返回的垂直间距
    public static final String KEY_RSP_MESSAGE_VERTICAL_SPACING = "rsp.message.vertical.spacing";
    // 自调用Message的水平宽度
    public static final String KEY_SELF_CALL_HORIZONTAL_WIDTH = "self.call.horizontal.width";
    // 自调用消息的垂直高度
    public static final String KEY_SELF_CALL_VERTICAL_HEIGHT = "self.call.vertical.height";
    // Activation的宽度
    public static final String KEY_ACTIVATION_WIDTH = "activation.width";
    // 两个部分之间的额外垂直间距
    public static final String KEY_PARTS_EXTRA_VERTICAL_SPACING = "parts.extra.vertical.spacing";

    //	线条宽度-Lifeline
    public static final String KEY_LINE_WIDTH_OF_LIFELINE = "line.width.of.lifeline";
    //	线条宽度-Activation
    public static final String KEY_LINE_WIDTH_OF_ACTIVATION = "line.width.of.activation";
    //	线条宽度-Message
    public static final String KEY_LINE_WIDTH_OF_MESSAGE = "line.width.of.message";
    //	线条颜色-Lifeline
    public static final String KEY_LINE_COLOR_OF_LIFELINE = "line.color.of.lifeline";
    //	线条颜色-Activation
    public static final String KEY_LINE_COLOR_OF_ACTIVATION = "line.color.of.activation";
    //	线条颜色-Message
    public static final String KEY_LINE_COLOR_OF_MESSAGE = "line.color.of.message";
    //	方框背景颜色-Lifeline
    public static final String KEY_BOX_COLOR_OF_LIFELINE = "box.color.of.lifeline";
    //	方框背景颜色-Activation
    public static final String KEY_BOX_COLOR_OF_ACTIVATION = "box.color.of.activation";
    //	文字字体-Lifeline
    public static final String KEY_TEXT_FONT_OF_LIFELINE = "text.font.of.lifeline";
    //	文字字体-Message
    public static final String KEY_TEXT_FONT_OF_MESSAGE = "text.font.of.message";
    //	文字大小-Lifeline
    public static final String KEY_TEXT_SIZE_OF_LIFELINE = "text.size.of.lifeline";
    //	文字大小-Message
    public static final String KEY_TEXT_SIZE_OF_MESSAGE = "text.size.of.message";
    //	文字颜色-Lifeline
    public static final String KEY_TEXT_COLOR_OF_LIFELINE = "text.color.of.lifeline";
    //	文字颜色-Message
    public static final String KEY_TEXT_COLOR_OF_MESSAGE = "text.color.of.message";

    private USDDIConstants() {
        throw new IllegalStateException("illegal");
    }
}
