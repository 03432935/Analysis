import com.analysis.common.utils.DateUtils;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.service.BatchQueryImportService;
import com.analysis.service.service.ImportExcelService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geologic.hazard.analysis.web.StartApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/21 15:39
 */
@SpringBootTest
@ContextConfiguration(classes = StartApp.class)
public class ImportInsertTest {

    @Autowired
    private ImportExcelService importExcelService;

    @Autowired
    private BatchQueryImportService batchQueryImportService;

    @Autowired
    private ImportDtoMapper importDtoMapper;

    @Test
    public void batchInserTest() throws Exception {
        ImportDto importDto = new ImportDto();
        importDto.setSenId("3");
        List<ImportDto> list = new ArrayList<>();
        list.add(importDto);
        importDtoMapper.batchInsert(list);
        System.out.println("insertMapper:");
        ImportDto data = new ImportDto();
        data.setSenId("2");
        data.setDel(1);
        list.add(data);
        importExcelService.save(list);
        System.out.println("serviceInsert:");
    }

    @Test
    public void query() {
        ImportDto importDto = new ImportDto();
        importDto.setSenId("1110000201");
//        List<ImportData> list = importDataMapper.query(importData);
//        System.out.println(list);
        IPage<ImportDto> list1 = batchQueryImportService.query(importDto);

        System.out.println(list1);
    }


    @Test
    public void dateTimeChooseTest() {
        List<ImportDto> list = importDtoMapper.selectList(null);
        System.out.println("测试总量查询：" + list);
        List<String> senIdList = list.stream().map(ImportDto::getSenId).distinct().collect(Collectors.toList());
        System.out.println("senId列表：" + senIdList);
        //对于每个senid来说
        for (String senId : senIdList) {
            //获取senid的值
            List<ImportDto> dtoList = list.stream().filter(importDto -> importDto.getSenId().equals(senId)).collect(Collectors.toList());
            System.out.println("对应的值" + dtoList);
            Long count = dtoList.stream().map(ImportDto::getVData).count();
            //总和
            Double sumV = dtoList.stream().mapToDouble(ImportDto::getVData).sum();
            //最大值
            Double maxV = dtoList.stream().mapToDouble(ImportDto::getVData).max().orElse(0.0);
            //最小值
            Double minV = dtoList.stream().mapToDouble(ImportDto::getVData).min().orElse(0.0);
            //总平均值
            Double avgV = dtoList.stream().mapToDouble(ImportDto::getVData).average().orElse(0.0);
            //当日平均值

            //当月平均值

            //当年平均值

            for (ImportDto importDto : dtoList) {
                Double avgVDay = dtoList.stream().filter(
                                importDto1 ->
                                    DateUtils.dateTimeToStrDay(importDto.getTTime()).equals(DateUtils.dateTimeToStrDay(importDto1.getTTime())))
                        .mapToDouble(ImportDto::getVData).average().orElse(0.0);

                System.out.println("avgDay"+avgVDay);
            }

            System.out.println(1);
        }
    }
}
