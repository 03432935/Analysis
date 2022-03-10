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
    public void query(){
        ImportDto importDto = new ImportDto();
        importDto.setSenId("1110000201");
//        List<ImportData> list = importDataMapper.query(importData);
//        System.out.println(list);
        IPage<ImportDto> list1 = batchQueryImportService.query(importDto);

        System.out.println(list1);
    }

    @Test
    public void selectid(){

    }
}
