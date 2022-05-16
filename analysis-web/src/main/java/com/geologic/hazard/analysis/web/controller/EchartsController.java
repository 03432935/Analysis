package com.geologic.hazard.analysis.web.controller;

import com.analysis.common.utils.ResultUtils;
import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.dao.validator.annotation.StringTrim;
import com.analysis.service.service.*;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ThreeSigma.ThreeSigmaImpl;
import com.analysis.service.utils.DisplayTool;
import com.analysis.service.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/5 16:28
 */
@Controller
@RequestMapping("/echarts")
@Slf4j
public class EchartsController {

    @Autowired
    private EchartsSendService echartsSendService;

    @Autowired
    private BatchQueryAvgService batchQueryAvgService;

    @Autowired
    private ImportDtoMapper importDtoMapper;

    @Autowired
    private AlgorithmService algorithmService;

    @StringTrim
    @RequestMapping(value = "/data",method = RequestMethod.POST)
    @ResponseBody
    public String echartsSendData(@RequestBody(required = false) ImportDto importDto) throws Exception {
        List<EchartDto> dtoList = echartsSendService.judgeInput(importDto);
        log.info("EchartsController.echartsSendData.res:"+dtoList);
        System.out.println("input:"+importDto+"output:"+dtoList);
        return ResultUtils.successResult(dtoList);
    }

    @StringTrim
    @RequestMapping(value = "/algorithm",method = RequestMethod.POST)
    @ResponseBody
    public String showExceptionPoint(@RequestBody(required = false) ImportDto importDto) throws Exception {
        List<ImportDto> importDtoList = echartsSendService.beforeAlgorithm(importDto);
        List<EchartDto> echartDtos = algorithmService.AlgorithmRes(importDto.getShowException(),importDtoList);
        System.out.println(echartDtos);
        return ResultUtils.successResult(echartDtos);
    }

    @StringTrim
    @RequestMapping(value = "/prediction",method = RequestMethod.POST)
    @ResponseBody
    public String predctionModule(@RequestBody AvgDto avgDto) throws Exception {
        log.info("EchartsController.predctionModule.dto:{}",avgDto);
        List<AvgDto> res = batchQueryAvgService.getAvgList(avgDto);
        System.out.println("input:"+avgDto+"output:"+res);
        return ResultUtils.successResult(res);
    }


}
