import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ESD.ESDImpl;
import com.analysis.service.utils.DisplayTool;
import com.analysis.service.utils.FileTool;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

public class ESDImplTest {

    public double[] testData;

    @Before
    public void setUp() throws FileNotFoundException {
        testData = FileTool.getData("src/main/resources/data.json");
    }

    @Test
    public void timeSeriesAnalyse() {
        ESDImpl esdTool = new ESDImpl(10);
        esdTool.timeSeriesAnalyse(testData);
        DisplayTool.showResult(esdTool);
    }
}