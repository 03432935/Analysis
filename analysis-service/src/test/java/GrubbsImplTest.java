import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ESD.GrubbsImpl;
import com.analysis.service.utils.DisplayTool;
import com.analysis.service.utils.FileTool;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

public class GrubbsImplTest {

    public double[] testData;

    @Before
    public void setUp() throws FileNotFoundException {
        testData = FileTool.getData("src/main/resources/data.json");
    }

    @Test
    public void timeSeriesAnalyse() {
        //100个样本Grubbs表：90%:3.017,95%:3.207,97.5%:3.383,99%:3.6,99.5%:3.754
        GrubbsImpl grubbsTool = new GrubbsImpl(3.383);//100个样本执行概率为97.5%
        grubbsTool.timeSeriesAnalyse(testData);
        DisplayTool.showResult(grubbsTool);
    }
}