package com.analysis.service.handler;

import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.service.enums.StrategyEnum;
import com.analysis.service.factory.StrategyFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/28 17:18
 */
@Slf4j
public class AvgHandler extends AbstractStrategy<AvgDto>{

    @Override
    protected List<ImportDto> before() {
        log.info("获取数据----");
        return null;
    }

    @Override
    protected List<AvgDto> analysisStrategy(List<ImportDto> importDtoList) {
        log.info("策略avghandler开始---------");
        return null;
    }

    @Override
    protected void after(List<AvgDto> avgDtos) {
        log.info("保存处理好的数据---------");
    }

    @Override
    protected void compensate() {
        log.info("补偿措施-----");
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        StrategyFactory.register(StrategyEnum.AVGSTRATEGY.getCode(), this);
    }

}
