import com.analysis.service.utils.FileTool;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/16 21:08
 */
public class RandomForestImplTest {
    public double[] testData;

    @Before
    public void setUp() throws FileNotFoundException {
        testData = FileTool.getData("src/main/resources/data.json");
    }

    @Test
    public void test(){

    }






    //    int numTrees = 100;
//        RandomForest Rf = new RandomForest(numTrees, testData,testData);
//        Rf.C = categ;//the num of labels
//        Rf.M = Train.get(0).length-1;//the num of Attr
//        //属性扰动，每次从M个属性中随机选取Ms个属性，Ms = ln(m)/ln(2)
//        Rf.Ms = (int)Math.round(Math.log(Rf.M)/Math.log(2) + 1);//随机选择的属性数量
//        Rf.Start();
}
