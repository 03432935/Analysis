import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ExponentialMoving.HoltWintersImpl;
import com.analysis.service.utils.DisplayTool;
import com.analysis.service.utils.FileTool;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

/**
 * Created by Administrator on 2018/4/27.
 */
public class HoltWintersImplTest {

    private static final double ALPHA = 0.1;
    private static final double STEP = 0.01;
    private static final int TIMES = 1000;

    public double[] testData;

    @Before
    public void setUp() throws Exception {
        testData = FileTool.getData("src/main/resources/data.json");
    }

    @Test
    public void timeSeriesAnalyse() throws Exception {
        HoltWintersImpl holtWintersImpl = new HoltWintersImpl(ALPHA, STEP, TIMES);
        holtWintersImpl.timeSeriesAnalyse(testData);
        DisplayTool.showResult(holtWintersImpl);
    }

}