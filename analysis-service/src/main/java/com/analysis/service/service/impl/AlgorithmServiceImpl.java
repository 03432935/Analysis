package com.analysis.service.service.impl;

import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.service.service.AbnormalJudgeService;
import com.analysis.service.service.AlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/15 16:42
 */
@Service
public class AlgorithmServiceImpl implements AlgorithmService {

    @Autowired
    private AbnormalJudgeService abnormalJudgeService;

    //这个地方用策略模式更好，但是赶时间
    @Override
    public List<EchartDto> AlgorithmRes(String code, List<ImportDto> importDtoList) throws Exception {
        List<EchartDto> echartDtos = new ArrayList<>();

        switch (code){
            case "1":
                echartDtos = abnormalJudgeService.threeSigmaFunction(importDtoList);
                break;
            case "2":
                echartDtos = abnormalJudgeService.grubbsTestFunction(importDtoList);
                break;
            case "3":
                echartDtos = abnormalJudgeService.LOFFunction(importDtoList);
                break;
            case "4":
                echartDtos = abnormalJudgeService.iForestFunction(importDtoList);
                break;
            case "5":
                echartDtos = abnormalJudgeService.holtWintersFunction(importDtoList);
                break;
            case "6":
                echartDtos = abnormalJudgeService.ARMAFunction(importDtoList);
                break;
            case "7":
                //随机森林
                break;
            default:
                break;
        }
        return echartDtos;
    }
}
