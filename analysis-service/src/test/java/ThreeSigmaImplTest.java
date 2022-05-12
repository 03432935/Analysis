import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ThreeSigma.ThreeSigmaImpl;
import com.analysis.service.utils.DisplayTool;
import com.analysis.service.utils.FileTool;
import org.junit.Before;
import org.junit.Test;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/9 16:10
 */
public class ThreeSigmaImplTest {

    public double[] testData;

    @Before
    public void setUp() throws Exception {
        testData = FileTool.getData("src/main/resources/data.json");
    }

    @Test
    public void timeSeriesAnalyse() throws Exception {
        ThreeSigmaImpl threeSigmaImpl = new ThreeSigmaImpl();
        threeSigmaImpl.timeSeriesAnalyse(testData);
        DisplayTool.showResult(threeSigmaImpl);
    }
}
