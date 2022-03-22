package com.geologic.hazard.analysis.web.controller;

import com.analysis.common.utils.ResultUtils;
import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.validator.annotation.StringTrim;
import com.analysis.service.service.BatchQueryAvgService;
import com.analysis.service.service.ConversionParamService;
import com.analysis.service.service.EchartsSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "/prediction",method = RequestMethod.POST)
    @ResponseBody
    public String predctionModule(@RequestBody AvgDto avgDto){
        batchQueryAvgService.getAvgList(avgDto);
        return ResultUtils.successResult(avgDto);
    }
}
