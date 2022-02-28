package com.analysis.service.handler;

import com.analysis.dao.entity.ImportData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/28 16:43
 */
@Slf4j
public abstract class AbstractStrategy implements InitializingBean {

    public void run(){
        //先获取数据
        List<ImportData> importDataList = before();
        //然后处理数据
        String res = analysisStrategy(importDataList);
        //最后把处理好的数据存储到新的表
        try{
            after();
        }catch (Exception e){
            log.error("保存数据出错",e);
            compensate();
        }

    }

    protected abstract List<ImportData> before();

    protected abstract String analysisStrategy(List<ImportData> importDataList);

    protected abstract void after();

    protected abstract void compensate();


}
