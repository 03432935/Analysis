package com.analysis.service.factory;

import com.analysis.service.handler.AbstractStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/28 17:11
 */
public class StrategyFactory {

    private static Map<String, AbstractStrategy> strategyMap = new HashMap<>(10);

    public static AbstractStrategy getInvokeStrategy(String name){
        return strategyMap.get(name);
    }

    public static void register(String name,AbstractStrategy abstractStrategy){
        if (name.isBlank()&&abstractStrategy == null){
            return;
        }
        strategyMap.put(name,abstractStrategy);
    }

}
