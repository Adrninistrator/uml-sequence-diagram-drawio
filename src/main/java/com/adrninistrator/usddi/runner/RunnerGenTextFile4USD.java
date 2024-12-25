package com.adrninistrator.usddi.runner;

import com.adrninistrator.usddi.common.USDDIConstants;
import com.adrninistrator.usddi.dto.lifeline.LifelineName;
import com.adrninistrator.usddi.enums.MessageTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author adrninistrator
 * @date 2021/10/18
 * @description:
 */
public class RunnerGenTextFile4USD {

    private boolean inited = false;

    private boolean done = false;

    // 保存生命线的显示名称及别名列表
    private List<LifelineName> lifelineNameList;

    // 保存生命线的显示名称及别名Map
    private Map<String, String> savedLifelineMap;

    // 保存消息信息列表
    private List<String> messageList;

    private BufferedWriter writer;

    private int lifelineSeq = 0;

    private boolean writeDescriptionDone = false;

    /**
     * 初始化
     *
     * @param filePath 指定需要生成的文本文件路径
     * @return
     * @throws FileNotFoundException
     */
    public BufferedWriter init(String filePath) throws FileNotFoundException {
        if (inited) {
            return writer;
        }

        lifelineNameList = new ArrayList<>();
        savedLifelineMap = new HashMap<>();
        messageList = new ArrayList<>();

        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8));
        inited = true;
        return writer;
    }

    /**
     * 设置描述
     *
     * @param description 描述内容
     * @param link        链接
     */
    public void writeDescription(String description, String link) throws IOException {
        checkStage();

        if (writeDescriptionDone) {
            throw new RuntimeException("只能指定一条描述");
        }
        writeDescriptionDone = true;

        if (StringUtils.isBlank(description)) {
            throw new RuntimeException("描述为空");
        }

        writer.write(USDDIConstants.DESCRIPTION_FLAG);
        writer.write(description);
        if (StringUtils.isNotBlank(link)) {
            writer.write(USDDIConstants.LINK_FLAG);
            writer.write(link);
        }
        writer.write(USDDIConstants.NEW_LINE);
    }

    /**
     * 添加注释
     *
     * @param comment 注释内容
     * @throws IOException
     */
    public void writeComment(String comment) throws IOException {
        checkStage();

        writer.write(USDDIConstants.COMMENT_FLAG);
        writer.write(" ");
        writer.write(comment);
        writer.write(USDDIConstants.NEW_LINE);
    }

    /**
     * 添加同步请求消息
     *
     * @param startLifeline 起点生命线
     * @param endLifeline   终点生命线
     * @param message       消息内容
     * @param link          链接
     */
    public void addReqMessage(String startLifeline, String endLifeline, String message, String link) {
        addMessage(MessageTypeEnum.MTE_REQ, startLifeline, endLifeline, message, link);
    }

    /**
     * 添加返回消息
     *
     * @param endLifeline   终点生命线
     * @param startLifeline 起点生命线
     * @param message       消息内容
     * @param link          链接
     */
    public void addRspMessage(String endLifeline, String startLifeline, String message, String link) {
        addMessage(MessageTypeEnum.MTE_RSP, endLifeline, startLifeline, message, link);
    }

    /**
     * 添加自调用消息
     *
     * @param lifeline 生命线
     * @param message  消息内容
     * @param link     链接
     */
    public void addSelfCallMessage(String lifeline, String message, String link) {
        addMessage(MessageTypeEnum.MTE_SELF, lifeline, lifeline, message, link);
    }

    /**
     * 添加异步请求消息
     *
     * @param startLifeline 起点生命线
     * @param endLifeline   终点生命线
     * @param message       消息内容
     * @param link          链接
     */
    public void addAsyncMessage(String startLifeline, String endLifeline, String message, String link) {
        addMessage(MessageTypeEnum.MTE_ASYNC, startLifeline, endLifeline, message, link);
    }

    /**
     * 将添加的数据写入文件
     *
     * @throws IOException
     */
    public void write2File() throws IOException {
        checkStage();

        // 写入生命线
        for (LifelineName lifelineName : lifelineNameList) {
            writer.write(USDDIConstants.LIFELINE_TITLE_FLAG);
            writer.write(lifelineName.getDisplayedName());
            writer.write(USDDIConstants.LIFELINE_ALIAS_FLAG);
            writer.write(lifelineName.getNameAlias());
            writer.write(USDDIConstants.NEW_LINE);
        }

        // 写入消息
        for (String message : messageList) {
            writer.write(message);
            writer.write(USDDIConstants.NEW_LINE);
        }

        writer.close();

        done = true;
    }

    // 检查当前阶段是否允许操作
    private void checkStage() {
        if (!inited) {
            System.err.println("请先调用 init 方法完成初始化");
            throw new RuntimeException("请先调用 init 方法完成初始化");
        }

        if (done) {
            System.err.println("文件写入已结束");
            throw new RuntimeException("文件写入已结束");
        }
    }

    // 添加生命线，允许重复添加（会进行去重）
    private String addLifeline(String lifeline) {
        String lifelineAlias = savedLifelineMap.get(lifeline);
        if (lifelineAlias != null) {
            return lifelineAlias;
        }

        lifelineAlias = String.format("lifeline_%03d", ++lifelineSeq);
        savedLifelineMap.put(lifeline, lifelineAlias);

        LifelineName lifelineName = new LifelineName();
        lifelineName.setDisplayedName(lifeline);
        lifelineName.setNameAlias(lifelineAlias);
        lifelineNameList.add(lifelineName);

        return lifelineAlias;
    }

    // 添加消息
    private void addMessage(MessageTypeEnum messageTypeEnum, String startLifeline, String endLifeline, String message, String link) {
        checkStage();

        if (StringUtils.isBlank(startLifeline)) {
            throw new RuntimeException("起点生命线为空");
        }
        if (StringUtils.isBlank(endLifeline)) {
            throw new RuntimeException("终点生命线为空");
        }
        if (StringUtils.isBlank(message)) {
            throw new RuntimeException("消息内容为空");
        }

        // 添加消息起点及终点对应的生命线
        String startLifelineAlias = addLifeline(startLifeline);
        String endLifelineAlias = addLifeline(endLifeline);

        StringBuilder messageContent = new StringBuilder();
        messageContent.append(startLifelineAlias).append(messageTypeEnum.getFlag()).append(endLifelineAlias).
                append(USDDIConstants.MESSAGE_TEXT_FLAG).append(message);
        if (link != null) {
            messageContent.append(USDDIConstants.LINK_FLAG).append(link);
        }
        messageList.add(messageContent.toString());
    }

}
