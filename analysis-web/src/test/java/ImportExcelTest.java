import com.analysis.common.utils.DateUtils;
import com.analysis.service.service.ImportExcelService;
import com.analysis.service.utils.ExcelUtils;
import com.geologic.hazard.analysis.web.StartApp;
import org.burningwave.core.assembler.StaticComponentContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/25 15:50
 */
@SpringBootTest
@ContextConfiguration(classes = StartApp.class)
public class ImportExcelTest {

    @Autowired
    private ImportExcelService importExcelService;

    @Test
    public void importExcel() {
        long startTime = System.currentTimeMillis();   //获取开始时间
        StaticComponentContainer.Modules.exportAllToAll();
        ExcelUtils.readWithListener("C:\\Users\\28196\\Desktop\\1110000201.xlsx", importExcelService);
        System.out.println("success");
        long endTime = System.currentTimeMillis(); //获取结束时间
        System.out.println("数据导入所花时间" + (endTime - startTime) + "ms");
    }

    @Test
    public void dateChangeTest() {
        String s = "2015/9/8 18:00";
        System.out.println(s);

        System.out.println(DateUtils.strToStr(s));

    }
}
