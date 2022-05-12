import com.analysis.service.service.impl.AnomalyDetectServiceImpl.IsolationTree.IsolationTreeImpl;
import com.analysis.service.utils.DisplayTool;
import com.analysis.service.utils.FileTool;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

public class IsolationTreeImplTest {
    public double[] testData;

    @Before
    public void setUp() throws FileNotFoundException {
        testData = FileTool.getData("src/main/resources/data.json");
    }

    @Test
    public void timeSeriesAnalyse() {
        IsolationTreeImpl isolationTreeImpl = new IsolationTreeImpl();
        isolationTreeImpl.timeSeriesAnalyse(testData);
        DisplayTool.showResult(isolationTreeImpl);
    }
}