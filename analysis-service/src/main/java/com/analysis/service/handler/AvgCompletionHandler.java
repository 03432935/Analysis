package com.analysis.service.handler;

import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.enums.AbnormalDataEnum;
import com.analysis.service.enums.CompletionStrategyEnum;
import com.analysis.service.factory.StrategyFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/28 17:18
 */
@Slf4j
@Component
public class AvgCompletionHandler extends AbstractStrategy<ImportDto>{

    @Autowired
    private ImportDtoMapper importDtoMapper;

    @Override
    protected List<ImportDto> before(StrategyContext strategyContext) {
        log.info("获取数据开始----");
        QueryWrapper<ImportDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("v_data");
        List<ImportDto> list = importDtoMapper.selectList(queryWrapper);
        if (list.size() == 0){
            QueryWrapper<ImportDto> wrapper = new QueryWrapper<>();
            wrapper.eq("completion_strategy", CompletionStrategyEnum.AVGSTRATEGY.getCode());
            list = importDtoMapper.selectList(wrapper);
        }
        log.info("获取数据结果----{}",list);
        return list;
    }

    @Override
    protected List<ImportDto> analysisStrategy(List<ImportDto> importDtoList,StrategyContext strategyContext) {
        log.info("策略avghandler开始---------");
        //importDtoList为需要补全的数据
        for (ImportDto importDto : importDtoList){
            //先通过mapper获取需要补全数据的其他数据来求平均值
            QueryWrapper<ImportDto> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sen_id",importDto.getSenId());
            queryWrapper.isNotNull("v_data");
            queryWrapper.eq("abnormal",AbnormalDataEnum.NORMAL.getCode());
            queryWrapper.eq("completion_strategy", CompletionStrategyEnum.ORIGINAL.getCode());
            List<ImportDto> list = importDtoMapper.selectList(queryWrapper);
            //求平均值，把需要的数据set进去，但此时还没有落库
            Double avgValue = list.stream().mapToDouble(ImportDto::getVData).average().orElse(0.0);
            importDto.setVData(avgValue);
        }
        return importDtoList;
    }

    @Override
    protected void after(List<ImportDto> importDtoList,StrategyContext strategyContext) {
        log.info("update处理好的数据v数据补上，并修改策略标识---------");
        for (ImportDto importDto: importDtoList){
            UpdateWrapper<ImportDto> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("completion_strategy", CompletionStrategyEnum.AVGSTRATEGY.getCode());
            updateWrapper.set("v_data", importDto.getVData());
            updateWrapper.eq("sen_id", importDto.getSenId());
            updateWrapper.eq("t_time", importDto.getTTime());
            importDtoMapper.update(importDto, updateWrapper);
        }
    }

    @Override
    protected void compensate() {
        log.info("补偿措施-----");
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        StrategyFactory.register(CompletionStrategyEnum.AVGSTRATEGY.getName(), this);
    }

}
