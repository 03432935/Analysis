package com.geologic.hazard.analysis.web.controller;

import com.analysis.common.utils.ResultUtils;
import com.analysis.dao.entity.ImportData;
import com.analysis.service.service.BatchQueryImportService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/21 16:12
 */
@Controller
@RequestMapping("/batchquery")
@Slf4j
public class BatchQueryImportController {

    @Autowired
    private BatchQueryImportService batchQueryImportService;

    @ResponseBody
    @RequestMapping(path = "/import",method = RequestMethod.POST)
    public String batchQuery(@RequestBody ImportData importData){
        PageInfo<ImportData> pageInfo = batchQueryImportService.query(importData);
        System.out.println(pageInfo);
        log.info("pageinfo:{}",pageInfo);
        return ResultUtils.successResult(pageInfo);
    }

    @ResponseBody
    @PostMapping("/test")
    public String test(@RequestBody String tt){
        System.out.println(tt);
        return ResultUtils.successResult(tt.toString());
    }
}
