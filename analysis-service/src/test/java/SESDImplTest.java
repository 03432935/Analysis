import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ESD.SESDImpl;
import com.analysis.service.utils.DisplayTool;
import com.analysis.service.utils.FileTool;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

public class SESDImplTest {

    public double[] testData;

    @Before
    public void setUp() throws FileNotFoundException {
        testData = FileTool.getData("src/main/resources/data.json");

    }

    @Test
    public void timeSeriesAnalyse() {
        SESDImpl sesdTool = new SESDImpl();
        sesdTool.timeSeriesAnalyse(testData);
        DisplayTool.showResult(sesdTool);
    }
}