import com.analysis.common.utils.DateUtils;
import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.enums.PredictionStrategyEnum;
import com.analysis.service.factory.ThreadPoolFactory;
import com.analysis.service.service.ConversionParamService;
import com.analysis.service.service.StrategyService;
import com.analysis.service.utils.ADFCheck;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geologic.hazard.analysis.web.StartApp;
import org.junit.jupiter.api.Test;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/4/22 18:07
 */

@SpringBootTest
@ContextConfiguration(classes = StartApp.class)
public class ADFCheckTest {

    @Autowired
    private ImportDtoMapper importDtoMapper;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private ConversionParamService conversionParamService;

    @Test
    public void adfTest() {
        //这个是自己写的ADF，结果只求了p的值，没有求出adf的值，不完整，过程逻辑也较为繁琐。
        List<ImportDto> list = importDtoMapper.selectList(null);

        double[] vData = list.stream().map(ImportDto::getVData).mapToDouble(i -> i).toArray();

        double res = ADFCheck.ADFCheck(vData);
        System.out.println("p的值为：" + res);
        //结果大约为8.5*10的-19次方，很小，考虑该方法的正确性，以及趋势问题，在写该方法时没有加上趋势，
    }

    @Test
    public void JythonTest() throws Exception {
//        java类中直接用py脚本
        //todo:传给python脚本的数据量要有所限制，不然会导致调操作系统调用py的时候报错“CreateProcess error=206, 文件名或扩展名太长”

        QueryWrapper<ImportDto> queryWrapper = new QueryWrapper<>();

        String yearStart = "2015-09-08 00:00:00";
        String yearEnd = "2016-05-20 00:00:00";
        Date start = DateUtils.strToDateTime(yearStart);
        Date end = DateUtils.strToDateTime(yearEnd);

        queryWrapper.between("t_time", start, end);
        queryWrapper.eq("abnormal", "0");
//        异常值对预测结果影响较大
        List<ImportDto> list = importDtoMapper.selectList(queryWrapper);

        List<EchartDto> resampleRes = conversionParamService.resampling(list);
        //由于传参有限制，所以在java对数据预处理以及重采样，因为目前一天可能会有几个数据，也可能没有数据

        List<Double> vData = resampleRes.stream().map(EchartDto::getVData).collect(Collectors.toList());

        try {
            String path = this.getClass().getClassLoader().getResource("predictTest.py").getPath().substring(1).replace("/", "\\");
//            path 的结果"/D:/xuexi/analysis/analysis/analysis-web/target/test-classes/adf.py" 需要删去前面的斜杠
            System.out.println(path);
            String[] args = new String[]{"py", path, String.valueOf(vData)};
            Process proc = Runtime.getRuntime().exec(args);// 执行py文件

            //原先的写法容易因为errorStream的流卡死缓存区，导致sout不出来，所以分别开了两个线程进行输出
            final InputStream inputStream = proc.getInputStream();

            final InputStream errorStream = proc.getErrorStream();

            //由原来直接new thread()显式创建线程改成使用线程池

            ThreadPoolFactory executor = ThreadPoolFactory.getThreadPool();

            List<Runnable> runnables = new ArrayList<>();


            Runnable runnable1 = new Runnable() {
                @Override
                public void run() {
                    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                    try {
                        String line1 = null;
                        StringBuilder res = new StringBuilder();
                        while ((line1 = in.readLine()) != null) {
                            System.out.println(line1);
                            if (line1.equals("Result")){
                                res.append("begin");
                            }
                            if (!res.isEmpty()){
                                res.append(line1);
                            }
                        }
                        System.out.println("result"+res);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try{
                            in.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            };

            Runnable runnable2 = new Runnable() {
                @Override
                public void run() {
                    BufferedReader error = new BufferedReader(new InputStreamReader(errorStream));
                    try {
                        String line2 = null;
                        while ((line2 = error.readLine()) != null) {
                            System.out.println(line2);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try{
                            error.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            };

            runnables.add(runnable1);
            runnables.add(runnable2);

            executor.execute(runnables);

            proc.waitFor();
            proc.destroy();
            System.out.println("print print");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


//            new Thread() {
//                public void run() {
//                    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
//                    try {
//                        String line1 = null;
//                        while ((line1 = in.readLine()) != null) {
//                            System.out.println(line1);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } finally {
//                        try{
//                            in.close();
//                        }catch (IOException e){
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }.start();
//
//            new Thread() {
//                public void run() {
//                    BufferedReader error = new BufferedReader(new InputStreamReader(errorStream));
//                    try {
//                        String line2 = null;
//                        while ((line2 = error.readLine()) != null) {
//                            System.out.println(line2);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } finally {
//                        try{
//                            error.close();
//                        }catch (IOException e){
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }.start();
//            proc.waitFor();
//            proc.destroy();
//            System.out.println("print print");
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    @Test
    public void getResourcesPathTest() {
//        由"/D:/xuexi/analysis/analysis/analysis-web/target/test-classes/adf.py"改成"D:\xuexi\main.py"类似格式
        String path = this.getClass().getClassLoader().getResource("adf.py").getPath();
        System.out.println(path);
        String path1 = path.substring(1);
        System.out.println(path1);
        String path2 = path1.replace("/", "\\");
        System.out.println(path2);
    }

    @Test
    public void ADFStrategyTest() throws Exception {
        AvgDto avgDto = new AvgDto();
        Date date = DateUtils.strToDateTime("2015-11-01 06:00:00");
        avgDto.setStartTime(date);
        avgDto.setSenId("1110002104");
        //还需要改,时间和senid
        strategyService.predictionStrategyRun(PredictionStrategyEnum.ARMA.getCode(), avgDto);
    }

    @Test
    public void sampleTest() {
        int a = 18;
        int b = 23;
        try {
            String path = this.getClass().getClassLoader().getResource("adf.py").getPath().substring(1).replace("/", "\\");
            System.out.println(path);
            String[] args = new String[]{"py", path, String.valueOf(a), String.valueOf(b)};
            Process proc = Runtime.getRuntime().exec(args);// 执行py文件

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            String line = null;
            String errorMessage = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            while ((errorMessage = error.readLine()) != null) {
                System.out.println(errorMessage);
            }

            in.close();
            error.close();

            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        //        java类中写python语句
//        PythonInterpreter interpreter = new PythonInterpreter();
//        interpreter.exec("a=[5,2,3,9,4,0]; ");
//        interpreter.exec("print(sorted(a));");  //此处python语句是3.x版本的语法
//        interpreter.exec("print sorted(a);");   //此处是python语句是2.x版本的语法

        //        该方法不能调用三方库，比如numpy
//        PythonInterpreter interpreter = new PythonInterpreter();
//        interpreter.execfile("D:\\xuexi\\main.py");
//
//        // 第一个参数为期望获得的函数（变量）的名字，第二个参数为期望返回的对象类型
//        PyFunction pyFunction = interpreter.get("func", PyFunction.class);
//        int a = 5, b = 10;
//        //调用函数，如果函数需要参数，在Java中必须先将参数转化为对应的“Python类型”
//        PyObject pyobj = pyFunction.__call__(new PyInteger(a), new PyInteger(b));
//        System.out.println("the anwser is: " + pyobj);

//        Process proc;
//        try {
////            下列代码需要注意"python D:\xuexi\main.py"这样写无效，把python改成py才有效
//            proc = Runtime.getRuntime().exec("py D:\\xuexi\\main.py");// 执行py文件
//            //用输入输出流来截取结果
//            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//            String line = null;
//            while ((line = in.readLine()) != null) {
//                System.out.println(line);
//            }
//            in.close();
//            proc.waitFor();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }


        //当进程挂了，卡死，可能存在的问题：因为errorStream对应缓存没有清空，所以导致程序阻塞，挂起，跑不动了。
        //方法二，有可能是因为errorStream对应缓存区没有清空，只要及时读取标准错误流缓冲区的数据程序就不会被block
//        验证：该想法错误。
//        StringBuffer stringBuffer = new StringBuffer();
//        try{
//            String path = this.getClass().getClassLoader().getResource("predictTest.py").getPath().substring(1).replace("/", "\\");
////            path 的结果"/D:/xuexi/analysis/analysis/analysis-web/target/test-classes/adf.py" 需要删去前面的斜杠
//            System.out.println(path);
//            String[] args = new String[]{"py", path, String.valueOf(vData)};
//            Process proc = Runtime.getRuntime().exec(args);// 执行py文件
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()),4096);
//
//            String line = null;
//            int i = 0;
//            while ((line = in.readLine()) != null) {
//                if (0!=i){
//                    stringBuffer.append("\r\n");
//                }
//                i++;
//                stringBuffer.append(line);
//            }
//        }catch (Exception e){
//            stringBuffer.append(e.getMessage());
//        }
//        System.out.println(stringBuffer);
    }

    @Test
    public void DateOneEndTest() throws ParseException {
        Date end = DateUtils.strToDateTime("2015-11-06 00:00:00");
//        System.out.println(DateUtils.getOneDayEndTimeDate(end));
        //todo：这个函数有问题
    }
}
