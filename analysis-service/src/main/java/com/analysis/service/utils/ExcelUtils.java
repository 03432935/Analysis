package com.analysis.service.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.analysis.common.utils.FileUtil;
import com.analysis.dao.entity.ImportData;
import com.analysis.service.listener.ImportExcelListener;
import com.analysis.service.service.ImportExcelService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/1/20 21:21
 */
@Slf4j
public class ExcelUtils {

    public static void readWithoutListener(){
        String fileName = FileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
    // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
    // 这里每次会读取3000条数据 然后返回过来 直接调用使用数据就行
        EasyExcel.read(fileName, ImportData.class, new PageReadListener<ImportData>(dataList -> {
            for (ImportData demoData : dataList) {
                log.info("读取到一条数据{}", JSON.toJSONString(demoData));
            }
        })).sheet().doRead();
    }

    public static void read(ImportExcelService importExcelService) {
        String fileName = FileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
        // 一个文件一个reader
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(fileName, ImportData.class, new ImportExcelListener(importExcelService)).build();
            // 构建一个sheet 这里可以指定名字或者no
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            // 读取一个sheet
            excelReader.read(readSheet);
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
    }

    public static void readWithListener(ImportExcelService importExcelService){
        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 写法3：
        String fileName = FileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, ImportData.class, new ImportExcelListener(importExcelService)).sheet().doRead();
    }
}
