package com.analysis.service.handler;

import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.service.enums.CompletionStrategyEnum;
import com.analysis.service.enums.PredictionStrategyEnum;
import com.analysis.service.factory.StrategyFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/15 17:44
 */
@Slf4j
public class PredictionOneHandler extends AbstractStrategy<AvgDto> {

    @Override
    protected List<ImportDto> before() {
        log.info("准备要处理预测所用的数据---");
        return null;
    }

    @Override
    protected List<AvgDto> analysisStrategy(List<ImportDto> importDtoList) {
        log.info("对现有数据进行处理---");
        return null;
    }

    @Override
    protected void after(List<AvgDto> list) {
        log.info("预测的数据写在handler表---");
    }

    @Override
    protected void compensate() {
        log.info("补偿措施---");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        StrategyFactory.register(PredictionStrategyEnum.One.getName(), this);
    }
}
