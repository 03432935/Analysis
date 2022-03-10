package com.analysis.service.handler;

import com.analysis.dao.entity.ImportDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/28 16:43
 */
@Slf4j
public abstract class AbstractStrategy<T> implements InitializingBean {

    public void run(){
        //先获取数据
        List<ImportDto> importDtoList = before();
        //然后处理数据
        List<T> res = analysisStrategy(importDtoList);
        //最后把处理好的数据存储到新的表
        try{
            after(res);
        }catch (Exception e){
            log.error("保存数据出错",e);
            compensate();
        }

    }

    /**
     * 获取数据库数据
     * @return
     */
    protected abstract List<ImportDto> before();

    /**
     * 对数据进行处理,返回entity用泛型
     * @param importDtoList
     * @return
     */
    protected abstract List<T> analysisStrategy(List<ImportDto> importDtoList);

    /**
     * 处理后的数据进行存储数据库的另一张表
     */
    protected abstract void after(List<T> list);

    /**
     * 补偿措施，用于后置处理出现问题后的重试操作
     */
    protected abstract void compensate();


}
