package test.usddi.genbytext;

import com.adrninistrator.usddi.runner.RunnerGenUmlSequenceDiagram;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author adrninistrator
 * @date 2024/11/10
 * @description:
 */
public class TestGenDrawIoByTextFile {

    private final RunnerGenUmlSequenceDiagram runnerGenUmlSequenceDiagram = new RunnerGenUmlSequenceDiagram();

    @Test
    public void example() {
        Assert.assertTrue(runnerGenUmlSequenceDiagram.generate("src/test/resources/example/example.txt"));
    }

    @Test
    public void multiLine() {
        Assert.assertTrue(runnerGenUmlSequenceDiagram.generate("src/test/resources/example/multi_line.txt"));
    }
}
