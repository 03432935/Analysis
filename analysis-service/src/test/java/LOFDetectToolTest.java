import com.analysis.service.service.impl.AnomalyDetectServiceImpl.LOF.LOF;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.LOF.LOFDetectImpl;
import com.analysis.service.utils.FileTool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Administrator on 2018/4/27.
 */
public class LOFDetectToolTest {

    public double[] testData;

    @Before
    public void setUp() throws Exception {
        testData = FileTool.getData("src/main/resources/data.json");
    }

    @Test
    public void timeSeriesAnalyse() throws Exception {
        LOFDetectImpl lofDetectTool = new LOFDetectImpl(200, 20);
        lofDetectTool.timeSeriesAnalyse(testData);
    }


    @Test
    public void findKthPoint() {
        LOF lof = new LOF(1);
        double[][] knn = {{1.0, 2.0}, {3.1, 4.0}};
        double[] x = {1.0, 2.0};
        Assert.assertEquals(lof.findKthPoint(knn, x)[0], 1.0, 0.1);
    }

}