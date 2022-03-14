import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.mapper.AvgDtoMapper;
import com.analysis.service.enums.StrategyEnum;
import com.analysis.service.service.EchartsSendService;
import com.geologic.hazard.analysis.web.StartApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/11 16:25
 */
@SpringBootTest
@ContextConfiguration(classes = StartApp.class)
public class HandleMapperTest {

    @Autowired
    private AvgDtoMapper avgDtoMapper;

    @Autowired
    private EchartsSendService echartsSendService;

    @Test
    public void mapperTest() throws Exception {
        AvgDto avgDto = new AvgDto();
//        avgDto.setTTime(new Date());
//        avgDto.setStrategyCode(StrategyEnum.AVGSTRATEGY.getCode());
//        avgDto.setVData(1.3);
//        avgDtoMapper.insert(avgDto);

        List<AvgDto> list = avgDtoMapper.getAvgListByAvg(avgDto);
        System.out.println("AvgDtoMapperTest.list"+list);

        List<EchartDto> res1 = echartsSendService.judgeInput(avgDto);
        System.out.println("action 1 : "+ res1);

        avgDto.setSenId("1");
        List<EchartDto> res2 = echartsSendService.judgeInput(avgDto);
        System.out.println("action 2 : "+ res2);

        avgDto.setStrategyCode(StrategyEnum.AVGSTRATEGY.getCode());
        List<EchartDto> res3 = echartsSendService.judgeInput(avgDto);
        System.out.println("action 3 : "+ res3);
    }

}
