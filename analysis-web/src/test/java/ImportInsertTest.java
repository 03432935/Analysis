import com.analysis.dao.entity.ImportData;
import com.analysis.dao.mapper.ImportDataMapper;
import com.analysis.service.service.BatchQueryImportService;
import com.analysis.service.service.ImportExcelService;
import com.geologic.hazard.analysis.web.StartApp;
import com.github.pagehelper.PageInfo;
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
    private ImportDataMapper importDataMapper;

    @Test
    public void batchInserTest() throws Exception {
        ImportData importData = new ImportData();
        importData.setSenId("1");
        List<ImportData> list = new ArrayList<>();
        list.add(importData);
        importDataMapper.batchInsert(list);
        System.out.println("insertMapper:");
//        ImportData data = new ImportData();
//        data.setSenId("2");
//        data.setDel(1);
//        list.add(data);
        importExcelService.save(list);
        System.out.println("serviceInsert:");
    }

    @Test
    public void query(){
        ImportData importData = new ImportData();
        importData.setSenId("1");
        List<ImportData> list = importDataMapper.query(importData);
        System.out.println(list);
        PageInfo<ImportData> list1 = batchQueryImportService.query(importData);
        System.out.println(list1);
    }

    @Test
    public void selectid(){

    }
}
