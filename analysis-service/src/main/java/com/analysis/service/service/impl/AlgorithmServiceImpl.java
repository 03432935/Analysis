package com.analysis.service.service.impl;

import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.service.service.AbnormalJudgeService;
import com.analysis.service.service.AlgorithmService;
import com.analysis.service.service.CompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/15 16:42
 */
@Service
public class AlgorithmServiceImpl implements AlgorithmService {

    @Autowired
    private AbnormalJudgeService abnormalJudgeService;

    @Autowired
    private CompletionService completionService;

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
                //多数投票
                echartDtos = MajorityVote(importDtoList);
                break;
            default:
                break;
        }
        return echartDtos;
    }

    @Override
    public List<EchartDto> AlgorithmCompletionRes(ImportDto importDto) throws Exception {
        List<EchartDto> echartDtos = new ArrayList<>();
        switch (importDto.getCompletionStrategy()) {
            case "1":
                echartDtos = completionService.strategyAvgFunction(importDto);
                break;
            case "2":
                echartDtos = completionService.holtWintersCompletionFunction(importDto);
                break;
            case "3":
                echartDtos = completionService.armaCompletionFunction(importDto);
                break;
            case "4":
                echartDtos = completionService.randomForestFunction(importDto);
                break;
            default:
                break;
        }
        return echartDtos;
    }


    private List<EchartDto> MajorityVote(List<ImportDto> importDtoList) throws Exception {
        List<EchartDto> echartDtos = new ArrayList<>();
        echartDtos.addAll(abnormalJudgeService.threeSigmaFunction(importDtoList));
        echartDtos.addAll(abnormalJudgeService.grubbsTestFunction(importDtoList));
        echartDtos.addAll(abnormalJudgeService.LOFFunction(importDtoList));
        echartDtos.addAll(abnormalJudgeService.iForestFunction(importDtoList));
        echartDtos.addAll(abnormalJudgeService.holtWintersFunction(importDtoList));
        echartDtos.addAll(abnormalJudgeService.ARMAFunction(importDtoList));
        Map<Date, Integer> map = new HashMap<>();

        List<EchartDto> res = new ArrayList<>();
        for (EchartDto echartDto :echartDtos){
            Integer count = map.get(echartDto.getTimeData());
            map.put(echartDto.getTimeData(), (count == null) ? 1 : count + 1);

            if(count!=null && map.get(echartDto.getTimeData())==3){
                System.out.println("voting result:"+echartDto);
                res.add(echartDto);
            }
        }
        System.out.println("res:"+res);
        res.stream().distinct().collect(Collectors.toList());
        System.out.println(res.size());
        return res;
    }

}
