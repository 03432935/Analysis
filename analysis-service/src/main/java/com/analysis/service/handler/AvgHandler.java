package com.analysis.service.handler;

import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.enums.CompletionStrategyEnum;
import com.analysis.service.factory.StrategyFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/28 17:18
 */
@Slf4j
public class AvgHandler extends AbstractStrategy<ImportDto>{

    @Autowired
    private ImportDtoMapper importDtoMapper;

    @Override
    protected List<ImportDto> before() {
        log.info("获取数据开始----");
        QueryWrapper<ImportDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("v_data",null);
        List<ImportDto> list = importDtoMapper.selectList(queryWrapper);
        log.info("获取数据结果----{}",list);
        return list;
    }

    @Override
    protected List<ImportDto> analysisStrategy(List<ImportDto> importDtoList) {
        log.info("策略avghandler开始---------");

        return null;
    }

    @Override
    protected void after(List<ImportDto> importDtoList) {
        log.info("update处理好的数据v数据补上，并修改策略标识---------");
        for (ImportDto importDto: importDtoList){
            importDto.setCompletionStrategy(CompletionStrategyEnum.AVGSTRATEGY.getCode());
            importDtoMapper.update(importDto,null);
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
