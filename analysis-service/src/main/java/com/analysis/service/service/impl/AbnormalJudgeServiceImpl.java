package com.analysis.service.service.impl;

import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.service.AbnormalJudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/7 14:56
 */
@Service
public class AbnormalJudgeServiceImpl implements AbnormalJudgeService {

    @Autowired
    private ImportDtoMapper importDtoMapper;

    @Override
    public List<ImportDto> threeSigmaFunction(List<ImportDto> data) {
        List<ImportDto> res = new ArrayList<>();
        //平均值
        Double avgValue = data.stream().mapToDouble(ImportDto::getVData).average().orElse(0.0);
        //标准差
        Double standardValue = standardVariance(data,avgValue);
        for (ImportDto d : data){
            if(Math.abs(d.getVData()-avgValue)>(3*standardValue)) {
                res.add(d);
                System.out.println("该数组中的"+d+"元素在3-sigma准则中属于异常值");
            }
        }
        return res;
    }

    @Override
    public List<ImportDto> EWMAFunction(List<ImportDto> data) {
        return null;
    }

    @Override
    public List<ImportDto> polynomialFunction(List<ImportDto> data) {
        return null;
    }

    @Override
    public List<ImportDto> XGBoostFunction(List<ImportDto> data) {
        return null;
    }

    /**
     * 求标准差
     * @param data
     * @return
     */
    private Double standardVariance(List<ImportDto> data,Double avgValue){
        double total = 0.0;
        for (ImportDto d : data){
            total += Math.pow(d.getVData()-avgValue,2);
        }
        return Math.sqrt(total/(data.size()-1));
    }

}
