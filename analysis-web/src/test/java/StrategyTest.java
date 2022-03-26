import com.analysis.service.enums.CompletionStrategyEnum;
import com.analysis.service.enums.OperationEnum;
import com.analysis.service.service.StrategyService;
import com.geologic.hazard.analysis.web.StartApp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/23 21:09
 */
@SpringBootTest
@ContextConfiguration(classes = StartApp.class)
@Slf4j
public class StrategyTest {

    @Autowired
    private StrategyService strategyService;


    @Test
    public void detectionHandlerTest() throws Exception {
        strategyService.abnormalDetectionRun(OperationEnum.ANOMALY_DETECTION.getCode());
        log.info("定时执行捕捉异常任务测试");
    }

    @Test
    public void avgCompletionHandlerTest() throws Exception {
        strategyService.completionStrategyRun(CompletionStrategyEnum.AVGSTRATEGY.getCode());
        log.info("avgCompletionHandlerTest");
    }
}
