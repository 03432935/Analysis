import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ARIMA.ARIMAImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ESD.ESDImpl;
import com.analysis.service.utils.DisplayTool;
import com.analysis.service.utils.FileTool;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/16 0:24
 */
public class ARIMAImplTest {

    public double[] testData;

    @Before
    public void setUp() throws FileNotFoundException {
        testData = FileTool.getData("src/main/resources/data.json");
    }

    @Test
    public void timeSeriesAnalyse() {
        ARIMAImpl arima = new ARIMAImpl();
        arima.timeSeriesAnalyse(testData);
//        DisplayTool.showResult(arima);
    }
}
