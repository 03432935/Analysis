package com.analysis.service.handler;

import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.ImportDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.naming.Context;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/28 16:43
 */
@Slf4j
public abstract class AbstractStrategy<T> implements InitializingBean {

    public void run(AvgDto avgDto) throws Exception {
        //上下文
        StrategyContext strategyContext = new StrategyContext();
        strategyContext.setTime(avgDto.getStartTime());
        strategyContext.setSenId(avgDto.getSenId());
        //先获取数据
        List<ImportDto> importDtoList = before(strategyContext);
        //然后处理数据
        List<T> res = analysisStrategy(importDtoList,strategyContext);
        //补全策略的话，更改原表数据，预测策略的话把处理好的数据存储到新的表
        try{
            after(res,strategyContext);
        }catch (Exception e){
            log.error("保存数据出错",e);
            compensate();
        }
    }


//    /**
//     * 写一个专门用于预测的通用方法（好像又没必要，先不写
//     * @throws Exception
//     */
//    public void predict() throws Exception{
//
//    }

    /**
     * 获取数据库数据
     * @return
     */
    protected abstract List<ImportDto> before(StrategyContext strategyContext);

    /**
     * 对数据进行处理,返回entity用泛型
     * @param importDtoList
     * @return
     */
    protected abstract List<T> analysisStrategy(List<ImportDto> importDtoList,StrategyContext strategyContext) throws Exception;

    /**
     * 处理后的数据进行存储数据库的另一张表
     */
    protected abstract void after(List<T> list,StrategyContext strategyContext) throws Exception;

    /**
     * 补偿措施，用于后置处理出现问题后的重试操作
     */
    protected abstract void compensate();


}
