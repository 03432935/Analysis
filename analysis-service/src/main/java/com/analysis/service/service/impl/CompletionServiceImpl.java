package com.analysis.service.service.impl;

import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.service.service.CompletionService;
import com.analysis.service.service.EchartsSendService;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.RandomForest.RandomForest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/16 22:45
 */
@Service
public class CompletionServiceImpl implements CompletionService {

    @Autowired
    private EchartsSendService echartsSendService;
    /**
     * 随机森林
     * @param importDto 入参是查询条件
     * @return
     */
    @Override
    public List<EchartDto> randomForestFunction(ImportDto importDto) {
        List<ImportDto> importDtoList = searchForNonData(importDto);
        RandomForest rf = new RandomForest();
        double[][] instances = new double[1][];
        int[] targets = new int[importDtoList.size()];
        for (int i = 0; i < importDtoList.size(); i++) {
            if ("0".equals(importDtoList.get(i).getCompletionStrategy())){
                //正常
                targets[i] = 0;
            }else if ("1".equals(importDtoList.get(i).getCompletionStrategy())){
                //异常缺失
                targets[i] = 1;
            }
        }
        rf.train(instances, targets, 30, 4, 4, 12);
//        for(int i=0;i<rf.trees.length;i++){
//            RandomForest.printTree(rf.trees[i], String.valueOf(i));
//        }
//
//        //System.out.println(rf.trees_.length);
//        int correct = 0;
//        int wrong = 0;
//        for (int i = 0; i < test_targets.length; i++) {
//            int actual = rf.predicateBySavedData(test_instances[i]);
//            System.out.println("actual: " + actual + ", expected: " + test_targets[i]);
//            if (actual == test_targets[i]) {
//                correct++;
//            } else {
//                wrong++;
//            }
//        }
//        System.out.println("correct:" + correct + ", wrong:" + wrong + ", accuracy:" + 1.0
//                * correct / (correct + wrong));
        return null;
    }

    //先查该时间应该有的数据，一整条都没有
    private List<ImportDto> searchForNonData(ImportDto importDto){
        int needComp = 0;
        //根据查询条件拿到数据
        List<ImportDto> importDtoList = echartsSendService.beforeAlgorithm(importDto);
        //检查数据之间缺失的
        for (int i = 0; i < importDtoList.size()-1; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(importDtoList.get(i).getTTime());
            cal.add(Calendar.HOUR, 12);//往后推12小时看看有没有数据
            Date tempData = cal.getTime();
            if(tempData.equals(importDtoList.get(i+1).getTTime())){
                continue;
            }else{
                System.out.println("缺数据");
                ImportDto importDto1 = new ImportDto();
                importDto1.setTTime(tempData);
                importDto1.setSenId(importDto.getSenId());
                importDto1.setCompletionStrategy("1");
                importDtoList.add(i+1,importDto1);
                needComp++;
            }
        }
        System.out.println(needComp);
        return importDtoList;
    }
}
