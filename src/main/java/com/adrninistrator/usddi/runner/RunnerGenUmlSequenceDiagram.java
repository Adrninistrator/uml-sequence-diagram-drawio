package com.adrninistrator.usddi.runner;

import com.adrninistrator.usddi.common.Constants;
import com.adrninistrator.usddi.conf.ConfManager;
import com.adrninistrator.usddi.conf.ConfPositionInfo;
import com.adrninistrator.usddi.dto.MessageInText;
import com.adrninistrator.usddi.dto.UsedVariables;
import com.adrninistrator.usddi.enums.MessageTypeEnum;
import com.adrninistrator.usddi.handler.EndAllHandler;
import com.adrninistrator.usddi.handler.EndPartHandler;
import com.adrninistrator.usddi.handler.LifelineHandler;
import com.adrninistrator.usddi.handler.base.BaseMessageHandler;
import com.adrninistrator.usddi.handler.message.AsyncMessageHandler;
import com.adrninistrator.usddi.handler.message.ReqMessageHandler;
import com.adrninistrator.usddi.handler.message.RspMessageHandler;
import com.adrninistrator.usddi.handler.message.SelfCallMessageHandler;
import com.adrninistrator.usddi.jaxb.generator.DrawIoUSDXmlGen;
import com.adrninistrator.usddi.util.CommonUtil;

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

    // 记录是否已处理完毕前面的lifeline的name
    private boolean handleLifelineNameDone = false;

    private AsyncMessageHandler asyncMessageHandler = new AsyncMessageHandler();
    private ReqMessageHandler reqMessageHandler = new ReqMessageHandler();
    private RspMessageHandler rspMessageHandler = new RspMessageHandler();
    private SelfCallMessageHandler selfCallMessageHandler = new SelfCallMessageHandler();

    private LifelineHandler lifelineHandler = new LifelineHandler();
    private EndAllHandler endAllHandler = new EndAllHandler();
    private EndPartHandler endPartHandler = new EndPartHandler();

    private UsedVariables usedVariables = UsedVariables.getInstance();

    private ConfManager confManager = ConfManager.getInstance();

    private ConfPositionInfo confPositionInfo = ConfPositionInfo.getInstance();

    private DrawIoUSDXmlGen drawIoUSDXmlGen = new DrawIoUSDXmlGen();

    public static void main(String[] args) {
        int argNum = 0;
        if (args != null) {
            argNum = args.length;
        }
        if (argNum != 1) {
            System.err.println("请在参数中指定需要生成UML时序图的文本文件，支持指定一个文件，当前参数数量 " + argNum);
            return;
        }

        RunnerGenUmlSequenceDiagram runner = new RunnerGenUmlSequenceDiagram();
        String txtFilePath = args[0];
        String outputFilePath = txtFilePath + "-" + CommonUtil.currentTime() + ".drawio";
        runner.generate(txtFilePath, outputFilePath);
    }

    /**
     * 根据文本生成UML时序图文件
     *
     * @param txtFilePath    输入文本文件路径
     * @param outputFilePath 生成的UML时序图文件路径
     * @return
     */
    public boolean generate(String txtFilePath, String outputFilePath) {
        if (!confManager.handlePositionConf() || !confManager.handleStyleConf()) {
            return false;
        }

        File txtFile = new File(txtFilePath);
        if (!txtFile.exists()) {
            System.err.println("指定的文件不存在 " + txtFilePath);
            return false;
        }
        if (!txtFile.isFile()) {
            System.err.println("指定的不是文件 " + txtFilePath);
            return false;
        }

        int lineNum = 0;
        // 标记上一行是否为空行
        boolean lastLineIsEmpty = false;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(txtFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineNum++;
                if (line.startsWith(Constants.COMMENT_FLAG)) {
                    continue;
                }

                if (!lastLineIsEmpty && line.trim().isEmpty()) {
                    // 若上一行非空行，且当前行为空行，代表部分结束
                    if (!endPartHandler.handle()) {
                        return false;
                    }
                    // 标记上一行为空行
                    lastLineIsEmpty = true;
                    continue;
                }

                // 标记上一行非空行
                lastLineIsEmpty = false;

                if (line.startsWith(Constants.LIFELINE_TITLE_FLAG)) {
                    // 当前行为lifeline的name
                    if (handleLifelineNameDone) {
                        System.err.println("lifeline的name已处理完毕，第" + lineNum + "行还是lifeline的name: " + line);
                        return false;
                    }

                    // 添加Lifeline
                    if (!lifelineHandler.addLifeline(line)) {
                        System.err.println("第" + lineNum + "行lifeline name处理失败: " + line);
                        return false;
                    }
                    continue;
                }

                // 当前行为Message
                if (!handleMessage(line, lineNum)) {
                    return false;
                }
            }

            // 若上一行为非空行，执行部分结束的操作
            if (!lastLineIsEmpty && !endPartHandler.handle()) {
                return false;
            }

            // 全部结束
            endAllHandler.handle();

            // 生成drawio的XML文件
            drawIoUSDXmlGen.generate(outputFilePath);

            System.out.println("处理完毕");
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
            System.err.println("未指定lifeline的name");
            return false;
        }

        if (!handleLifelineNameDone) {
            // 记录lifeline的name已处理完毕
            handleLifelineNameDone = true;

            // 初始时，当前处理到的y坐标值设置为Lifeline方框高度
            usedVariables.setCurrentY(confPositionInfo.getLifelineBoxHeight());
        }

        // 获得Message中的标志
        MessageInText messageInText = BaseMessageHandler.parseMessage(line);
        if (messageInText == null) {
            System.err.println("第" + lineNum + "行lifeline name处理失败: " + line);
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
            System.err.println("第" + lineNum + "行Message处理失败: " + line);
            return false;
        }
        return true;
    }
}
