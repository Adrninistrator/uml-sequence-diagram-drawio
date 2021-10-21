package test.usddi.gen_text_file;

import com.adrninistrator.usddi.common.USDDIConstants;
import com.adrninistrator.usddi.runner.RunnerGenTextFile4USD;
import com.adrninistrator.usddi.runner.RunnerGenUmlSequenceDiagram;

import java.io.BufferedWriter;

/**
 * @author adrninistrator
 * @date 2021/10/19
 * @description:
 */
public class TestGenTextFile4USD {

    public static void main(String[] args) {
        String filePath = "test-" + System.currentTimeMillis() + ".txt";
        System.out.println(filePath);

        RunnerGenTextFile4USD runnerGenTextFile4USD = new RunnerGenTextFile4USD();

        try (BufferedWriter writer = runnerGenTextFile4USD.init(filePath)) {
            // 添加注释
            runnerGenTextFile4USD.writeComment("111");
            runnerGenTextFile4USD.writeComment("222");
            runnerGenTextFile4USD.writeComment("333");

            // 添加不同类型的消息
            runnerGenTextFile4USD.addReqMessage("a", "b", "请求1");
            runnerGenTextFile4USD.addRspMessage("a", "b", "返回1");
            runnerGenTextFile4USD.addSelfCallMessage("a", "自调用1<br>aaa");
            runnerGenTextFile4USD.addAsyncMessage("a", "c", "异步请求1<br>bbb");

            // 将添加的数据写入文件
            runnerGenTextFile4USD.write2File();

            // 根据文本生成UML时序图文件
            new RunnerGenUmlSequenceDiagram().generate(filePath, filePath + USDDIConstants.EXT_DRAWIO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
