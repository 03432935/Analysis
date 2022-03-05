package com.geologic.hazard.analysis.web.controller;

import com.analysis.common.utils.ResultUtils;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportData;
import com.analysis.service.service.BatchQueryImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    private BatchQueryImportService batchQueryImportService;

    @RequestMapping(value = "/data",method = RequestMethod.POST)
    @ResponseBody
    public String echartsSendData(){
        List<ImportData> list = batchQueryImportService.getList();
        log.info("EchartsController.echartsSendData.list"+list);
        List<EchartDto> echartDtos = batchQueryImportService.getEchartData(list);
        log.info("EchartsController.echartsSendData.echartDtos"+echartDtos);
        return ResultUtils.successResult(echartDtos);
    }
}
