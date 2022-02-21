package com.geologic.hazard.analysis.web.controller;

import com.analysis.common.utils.ResultUtils;
import com.analysis.dao.entity.ImportData;
import com.analysis.service.service.BatchQueryImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/21 16:12
 */
@Controller
@RequestMapping("/batchquery")
public class BatchQueryImportController {

    @Autowired
    private BatchQueryImportService batchQueryImportService;

    @ResponseBody
    @PostMapping("/import")
    public String batchQuery(ImportData importData){
        return ResultUtils.successResult(batchQueryImportService.query(importData));
    }
}
