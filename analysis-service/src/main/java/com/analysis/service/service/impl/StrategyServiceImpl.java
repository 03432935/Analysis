package com.analysis.service.service.impl;

import com.analysis.dao.entity.ImportDto;
import com.analysis.service.enums.CompletionStrategyEnum;
import com.analysis.service.enums.OperationEnum;
import com.analysis.service.enums.PredictionStrategyEnum;
import com.analysis.service.factory.StrategyFactory;
import com.analysis.service.handler.AbstractStrategy;
import com.analysis.service.service.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/28 17:22
 */
@Slf4j
@Service
public class StrategyServiceImpl implements StrategyService {

    /**
     * 异常策略
     * @param code
     * @throws Exception
     */
    @Override
    public void abnormalDetectionRun(String code) throws Exception {
        OperationEnum operationEnum = OperationEnum.findEnumByCode(code);
        String name = operationEnum.getName();
        AbstractStrategy strategy = StrategyFactory.getInvokeStrategy(name);
        strategy.run();
        log.info("策略运行结束,return:xxx");
    }


    /**
     * 补全策略
     * @param code
     */
    @Override
    public void completionStrategyRun(String code) throws Exception {
        CompletionStrategyEnum completionStrategyEnum = CompletionStrategyEnum.findEnumByCode(code);
        String name = completionStrategyEnum.getName();
        //获取对应的策略handler
        AbstractStrategy strategy = StrategyFactory.getInvokeStrategy(name);
        //执行对应策略的方法
        strategy.run();
        log.info("策略运行结束,return:xxx");
    }

    /**
     * 预测策略
     * @param code
     * @throws Exception
     */
    @Override
    public void predictionStrategyRun(String code) throws Exception {
        PredictionStrategyEnum predictionStrategyEnum = PredictionStrategyEnum.findEnumByCode(code);
        String name = predictionStrategyEnum.getName();
        //获取对应的策略handler
        AbstractStrategy strategy = StrategyFactory.getInvokeStrategy(name);
        //执行对应策略的方法
        strategy.run();
        log.info("策略运行结束,return:xxx");
    }

    /*由于code很可能都一样，比如都是0或者1，逻辑上会出现问题
    private String checkInput(String code){
        OperationEnum operationEnum = OperationEnum.findEnumByCode(code);
        CompletionStrategyEnum completionStrategyEnum = CompletionStrategyEnum.findEnumByCode(code);
        PredictionStrategyEnum predictionStrategyEnum = PredictionStrategyEnum.findEnumByCode(code);
        if(operationEnum != null){
            return operationEnum.getName();
        }
        if(completionStrategyEnum != null){
            return completionStrategyEnum.getName();
        }
        if(predictionStrategyEnum != null){
            return predictionStrategyEnum.getName();
        }
    }
     */
}
