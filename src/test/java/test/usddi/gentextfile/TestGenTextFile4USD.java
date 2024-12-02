package test.usddi.gentextfile;

import com.adrninistrator.usddi.common.USDDIConstants;
import com.adrninistrator.usddi.runner.RunnerGenTextFile4USD;
import com.adrninistrator.usddi.runner.RunnerGenUmlSequenceDiagram;
import com.adrninistrator.usddi.util.USDDIUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedWriter;

/**
 * @author adrninistrator
 * @date 2021/10/19
 * @description:
 */
public class TestGenTextFile4USD {

    private final RunnerGenUmlSequenceDiagram runnerGenUmlSequenceDiagram = new RunnerGenUmlSequenceDiagram();

    @Test
    public void test() {
        String description = "<font color=\"#0000ff\" ><u>我是描述</u></font><br>end";

        generate(description, "abc");
        generate(description, null);
        generate(null, null);
    }

    private void generate(String description, String descriptionLink) {
        String filePath = "test-" + USDDIUtil.currentTime() + ".txt";
        System.out.println(filePath);

        RunnerGenTextFile4USD runnerGenTextFile4USD = new RunnerGenTextFile4USD();

        try (BufferedWriter writer = runnerGenTextFile4USD.init(filePath)) {
            if (description != null) {
                // 设置描述
                runnerGenTextFile4USD.writeDescription(description, descriptionLink);
            }

            // 添加注释
            runnerGenTextFile4USD.writeComment("111");
            runnerGenTextFile4USD.writeComment("222");
            runnerGenTextFile4USD.writeComment("333");

            // 添加不同类型的消息
            runnerGenTextFile4USD.addReqMessage("a", "b", "请求1", "链接a");
            runnerGenTextFile4USD.addRspMessage("a", "b", "返回1", null);
            runnerGenTextFile4USD.addSelfCallMessage("a", "自调用1<br>aaa", "");
            runnerGenTextFile4USD.addAsyncMessage("a", "c", "异步请求1<br>bbb", "链接b");

            // 将添加的数据写入文件
            runnerGenTextFile4USD.write2File();

            // 根据文本生成UML时序图文件
            Assert.assertTrue(runnerGenUmlSequenceDiagram.generate(filePath, filePath + USDDIConstants.EXT_DRAWIO));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
