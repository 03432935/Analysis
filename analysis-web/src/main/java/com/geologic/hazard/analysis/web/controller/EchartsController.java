package com.geologic.hazard.analysis.web.controller;

import com.analysis.common.utils.ResultUtils;
import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.service.service.BatchQueryAvgService;
import com.analysis.service.service.BatchQueryImportService;
import com.analysis.service.service.EchartsSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
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

    @RequestMapping(value = "/data",method = RequestMethod.POST)
    @ResponseBody
    public String echartsSendData(@RequestBody AvgDto avgDto){
        List<EchartDto> dtoList = echartsSendService.judgeInput(avgDto);
        log.info("EchartsController.echartsSendData.res:"+dtoList);
        return ResultUtils.successResult(dtoList);
    }
}
