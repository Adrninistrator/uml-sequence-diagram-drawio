package com.adrninistrator.usddi.runner;

import com.adrninistrator.usddi.common.USDDIConstants;
import com.adrninistrator.usddi.conf.ConfManager;
import com.adrninistrator.usddi.conf.ConfPositionInfo;
import com.adrninistrator.usddi.dto.message.MessageInText;
import com.adrninistrator.usddi.dto.variables.UsedVariables;
import com.adrninistrator.usddi.enums.MessageTypeEnum;
import com.adrninistrator.usddi.handler.DescriptionHandler;
import com.adrninistrator.usddi.handler.EndAllHandler;
import com.adrninistrator.usddi.handler.EndPartHandler;
import com.adrninistrator.usddi.handler.LifelineHandler;
import com.adrninistrator.usddi.handler.message.AsyncMessageHandler;
import com.adrninistrator.usddi.handler.message.ReqMessageHandler;
import com.adrninistrator.usddi.handler.message.RspMessageHandler;
import com.adrninistrator.usddi.handler.message.SelfCallMessageHandler;
import com.adrninistrator.usddi.jaxb.generator.DrawIoUSDXmlGen;
import com.adrninistrator.usddi.logger.DebugLogger;
import com.adrninistrator.usddi.util.USDDIUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author adrninistrator
 * @date 2021/9/15
 * @description:
 */
public class RunnerGenUmlSequenceDiagram {

    private ConfManager confManager = ConfManager.getInstance();

    private ConfPositionInfo confPositionInfo = ConfPositionInfo.getInstance();

    private UsedVariables usedVariables = null;

    private AsyncMessageHandler asyncMessageHandler = null;
    private ReqMessageHandler reqMessageHandler = null;
    private RspMessageHandler rspMessageHandler = null;
    private SelfCallMessageHandler selfCallMessageHandler = null;

    private DescriptionHandler descriptionHandler = null;
    private LifelineHandler lifelineHandler = null;
    private EndAllHandler endAllHandler = null;
    private EndPartHandler endPartHandler = null;

    private DrawIoUSDXmlGen drawIoUSDXmlGen = null;

    // 记录是否已处理过描述
    private boolean handleDescriptionDone = false;

    // 记录是否已处理完毕前面的lifeline的name
    private boolean handleLifelineNameDone = false;

    public static void main(String[] args) {
        int argNum = 0;
        if (args != null) {
            argNum = args.length;
        }
        if (argNum != 1) {
            System.err.println("请在参数中指定需要生成UML时序图的文本文件，支持指定一个文件，当前参数数量: " + argNum);
            return;
        }

        String txtFilePath = args[0];
        String outputFilePath = txtFilePath + "-" + USDDIUtil.currentTime() + USDDIConstants.EXT_DRAWIO;
        new RunnerGenUmlSequenceDiagram().generate(txtFilePath, outputFilePath);
    }

    // 初始化
    private void init() {
        UsedVariables.reset();

        usedVariables = UsedVariables.getInstance();

        asyncMessageHandler = new AsyncMessageHandler();
        reqMessageHandler = new ReqMessageHandler();
        rspMessageHandler = new RspMessageHandler();
        selfCallMessageHandler = new SelfCallMessageHandler();

        descriptionHandler = new DescriptionHandler();
        lifelineHandler = new LifelineHandler();
        endAllHandler = new EndAllHandler();
        endPartHandler = new EndPartHandler();

        drawIoUSDXmlGen = new DrawIoUSDXmlGen();
    }

    /**
     * 根据文本生成UML时序图文件
     *
     * @param txtFilePath    输入文本文件路径
     * @param outputFilePath 生成的UML时序图文件路径
     * @return
     */
    public boolean generate(String txtFilePath, String outputFilePath) {
        // 初始化
        init();

        DebugLogger.init();

        boolean success = doGenerate(txtFilePath, outputFilePath);

        DebugLogger.beforeExit();

        return success;
    }

    public boolean doGenerate(String txtFilePath, String outputFilePath) {

        if (!confManager.handlePositionConf() || !confManager.handleStyleConf()) {
            return false;
        }

        File txtFile = new File(txtFilePath);
        if (!txtFile.exists()) {
            System.err.println("指定的文件不存在: " + txtFilePath);
            return false;
        }
        if (!txtFile.isFile()) {
            System.err.println("指定的不是文件: " + txtFilePath);
            return false;
        }

        int lineNum = 0;
        // 标记上一行是否为空行
        boolean lastLineIsEmpty = false;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(txtFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineNum++;

                if (lineNum == 1 && line.startsWith("\uFEFF")) {
                    System.err.println("当前文件编码为UTF-8-BOM，请修改为UTF-8无BOM");
                    return false;
                }

                if (line.startsWith(USDDIConstants.COMMENT_FLAG)) {
                    continue;
                }

                if (!lastLineIsEmpty && line.trim().isEmpty()) {
                    // 若上一行非空行，且当前行为空行，代表部分结束
                    if (!endPartHandler.handle()) {
                        System.err.println("开始处理新的部分，第" + lineNum + "行处理失败: " + line);
                        return false;
                    }
                    // 标记上一行为空行
                    lastLineIsEmpty = true;
                    continue;
                }

                // 标记上一行非空行
                lastLineIsEmpty = false;

                if (line.startsWith(USDDIConstants.DESCRIPTION_FLAG)) {
                    // 当前行为描述
                    if (handleDescriptionDone) {
                        System.err.println("只允许在生命线及消息前指定一个描述，第" + lineNum + "行还是描述: " + line);
                        return false;
                    }

                    // 处理描述
                    descriptionHandler.handleDescription(line);

                    handleDescriptionDone = true;
                } else if (line.startsWith(USDDIConstants.LIFELINE_TITLE_FLAG)) {
                    // 当前行为lifeline的name
                    if (handleLifelineNameDone) {
                        System.err.println("生命线名称已处理完毕，第" + lineNum + "行还是生命线名称: " + line);
                        return false;
                    }

                    // 添加Lifeline
                    if (!lifelineHandler.addLifeline(line)) {
                        System.err.println("添加第" + lineNum + "行生命线名称失败: " + line);
                        return false;
                    }
                } else {
                    // 当前行为Message
                    if (!handleMessage(line, lineNum)) {
                        return false;
                    }
                }
            }

            // 循环结束
            // 若上一行为非空行，执行部分结束的操作
            if (!lastLineIsEmpty && !endPartHandler.handle()) {
                return false;
            }

            // 全部结束
            if (!endAllHandler.handle()) {
                return false;
            }

            // 生成drawio的XML文件
            if (!drawIoUSDXmlGen.generate(outputFilePath)) {
                return false;
            }

            System.out.println("生成UML时序图处理完毕 " + outputFilePath);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 对Message进行处理
     *
     * @param line
     * @param lineNum
     * @return
     */
    private boolean handleMessage(String line, int lineNum) {
        if (usedVariables.getLifelineInfoList().isEmpty()) {
            System.err.println("第" + lineNum + "行未指定生命线的名称: " + line);
            return false;
        }

        if (!handleLifelineNameDone) {
            // 记录lifeline的name已处理完毕
            handleLifelineNameDone = true;

            // 第一次处理消息时，当前处理到的y坐标值加上Lifeline方框高度
            usedVariables.addCurrentY(this.getClass(), "加上Lifeline方框高度", confPositionInfo.getLifelineBoxHeight());
        }

        // 获得Message中的标志
        MessageInText messageInText = USDDIUtil.getMessageInText(line);
        if (messageInText == null) {
            System.err.println("第" + lineNum + "行生命线名称处理失败: " + line);
            return false;
        }
        MessageTypeEnum messageType = messageInText.getMessageType();
        boolean success = false;
        switch (messageType) {
            case MTE_REQ:
                success = reqMessageHandler.handleMessage(messageInText);
                break;
            case MTE_RSP:
                success = rspMessageHandler.handleMessage(messageInText);
                break;
            case MTE_SELF:
                success = selfCallMessageHandler.handleMessage(messageInText);
                break;
            case MTE_ASYNC:
                success = asyncMessageHandler.handleMessage(messageInText);
                break;
        }
        if (!success) {
            System.err.println("第" + lineNum + "行消息处理失败: " + line);
            return false;
        }
        return true;
    }
}
