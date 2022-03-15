package com.analysis.service.service.impl;

import com.analysis.service.enums.CompletionStrategyEnum;
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

    @Override
    public void strategyRun(String code) {
//        todo:校验code
        CompletionStrategyEnum completionStrategyEnum = CompletionStrategyEnum.findEnumByCode(code);
        String name = completionStrategyEnum.getName();
        //获取对应的策略handler
        AbstractStrategy strategy = StrategyFactory.getInvokeStrategy(name);
        //执行对应策略的方法
        strategy.run();
        log.info("策略运行结束,return:xxx");
    }


}
