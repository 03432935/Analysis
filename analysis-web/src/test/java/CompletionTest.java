import com.analysis.common.utils.DateUtils;
import com.analysis.dao.entity.ImportDto;
import com.analysis.service.service.CompletionService;
import com.geologic.hazard.analysis.web.StartApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/16 23:13
 */
@SpringBootTest
@ContextConfiguration(classes = StartApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CompletionTest {

    @Autowired
    private CompletionService completionService;

    @Test
    public void completionTest() throws Exception {
        ImportDto importDto = new ImportDto();
        importDto.setSenId("1110002605");
        Date start = DateUtils.strToDateTime("2020-04-01 06:00:00");
        Date end = DateUtils.strToDateTime("2020-06-01 18:00:00");
        importDto.setStartTime(start);
        importDto.setEndTime(end);
//        completionService.randomForestFunction(importDto);
        completionService.strategyAvgFunction(importDto);
    }

    @Test
    public void holtCompTest() throws Exception {
        //368条  57
        ImportDto importDto = new ImportDto();
        importDto.setSenId("1110002605");
        Date start = DateUtils.strToDateTime("2018-01-01 06:00:00");
        Date end = DateUtils.strToDateTime("2018-07-01 06:00:00");
        importDto.setStartTime(start);
        importDto.setEndTime(end);
        completionService.holtWintersCompletionFunction(importDto);
    }

    @Test
    public void armaCompTest() throws Exception {
        ImportDto importDto = new ImportDto();
        importDto.setSenId("1110002605");
        Date start = DateUtils.strToDateTime("2020-04-01 06:00:00");
        Date end = DateUtils.strToDateTime("2020-06-01 18:00:00");
        importDto.setStartTime(start);
        importDto.setEndTime(end);
        completionService.armaCompletionFunction(importDto);
    }

    @Test
    public void RandomForestTest(){
        ImportDto importDto = new ImportDto();
        importDto.setSenId("1110002605");
        Date start = DateUtils.strToDateTime("2020-04-01 06:00:00");
        Date end = DateUtils.strToDateTime("2020-06-01 18:00:00");
        importDto.setStartTime(start);
        importDto.setEndTime(end);
        completionService.randomForestFunction(importDto);
    }
}
