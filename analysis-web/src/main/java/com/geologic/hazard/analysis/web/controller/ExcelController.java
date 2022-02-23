package com.geologic.hazard.analysis.web.controller;

import com.analysis.service.service.ImportExcelService;
import com.analysis.service.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/21 14:55
 */
@Controller
@RequestMapping("/excel")
public class ExcelController {
    /**
     * 文件上传
     * <p>
     * 1. 创建excel对应的实体对象
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器
     * <p>
     * 3. 直接读即可
     */
    @Autowired
    private ImportExcelService importExcelService;

    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public String upload(MultipartFile file) throws IOException {
        ExcelUtils.readWithInputStream(file.getInputStream(),importExcelService);
        return "success";
    }

}
