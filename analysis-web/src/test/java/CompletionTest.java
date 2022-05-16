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
    public void completionTest(){
        ImportDto importDto = new ImportDto();
        importDto.setSenId("1110002605");
        Date start = DateUtils.strToDateTime("2015-01-01 06:00:00");
        Date end = DateUtils.strToDateTime("2015-06-01 18:00:00");
        importDto.setStartTime(start);
        importDto.setEndTime(end);
        completionService.randomForestFunction(importDto);
    }
}
