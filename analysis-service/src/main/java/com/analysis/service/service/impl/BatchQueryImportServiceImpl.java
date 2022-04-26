package com.analysis.service.service.impl;

import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.enums.AbnormalDataEnum;
import com.analysis.service.service.BatchQueryImportService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/21 16:12
 */
@Service
@Slf4j
public class BatchQueryImportServiceImpl extends SuperServiceImpl<ImportDtoMapper, ImportDto> implements BatchQueryImportService {

    @Autowired
    private ImportDtoMapper importDtoMapper;

    @Autowired
    private BatchQueryImportService batchQueryImportService;

    @Override
    public IPage<ImportDto> query(ImportDto importDto){

        QueryWrapper<ImportDto> queryWrapper = new QueryWrapper<>();
        // 构建搜索条件
        if (!importDto.getSenId().isBlank()){
            queryWrapper.eq("sen_id", importDto.getSenId());
        }
        queryWrapper.orderByAsc("t_time");
        Page<ImportDto> page = new Page<>(importDto.getCurrentPage(), importDto.getPageSize());
//        IPage<ImportData> pageList = batchQueryImportService.page(page,queryWrapper); //两种表现形式
        IPage<ImportDto> pageList = importDtoMapper.selectPage(page,queryWrapper);
//        List<ImportData> list = pageList.getRecords();
        System.out.println("page:"+pageList);
        return pageList;

    }

    @Override
    public List<ImportDto> getList(String isShowException) {
        QueryWrapper<ImportDto> queryWrapper = new QueryWrapper<>();
        if ("0".equals(isShowException)) {
            //如果不展示异常值，条件上就加上只筛选正常值
            queryWrapper.eq("abnormal", AbnormalDataEnum.NORMAL);
        }
        List<ImportDto> list = importDtoMapper.selectList(queryWrapper);
        log.info("BatchQueryImportServiceImpl.getList:"+list);
        return list;

    }

    @Override
    public List<ImportDto> getSomeList(ImportDto dto) {
        if("1".equals(dto.getIsShowException())){
            dto.setAbnormal(null);
        }
        List<ImportDto> list = importDtoMapper.query(dto);
        log.info("BatchQueryImportServiceImpl.getSomeList:"+list);
        return list;
    }

    @Override
    public List<EchartDto> getEchartData(List<ImportDto> importDtoList) {
        List<EchartDto> echartDtos = importDtoList.stream()
                .map(
                        importData -> {
                            EchartDto echartDto = new EchartDto();
                            echartDto.setTimeData(importData.getTTime());
                            echartDto.setVData(importData.getVData());
                            return echartDto;
                        }
                )
                .collect(Collectors.toList());
        log.info("getEchartData.echartDtos:"+echartDtos);
        return echartDtos;
    }
}


//分页方法
//方法一：
//也可以直接返回PageInfo，注意doSelectPageInfo方法和doSelectPage
//        PageInfo<ImportData> pageInfo = PageHelper.startPage(1, 10).doSelectPageInfo(new ISelect() {
//            @Override
//            public void doSelect() {
//                importDataMapper.query(importData);
//            }
//        });
//        return pageInfo;

//方法二：
//        Page<ImportData> page = PageHelper.startPage(1, 10).doSelectPage(()-> importDataMapper.query(importData));
//        System.out.println(page);
//        PageInfo<ImportData> info = new PageInfo<>(page);
//        System.out.println(info);
//        return info;
//        System.out.println(page);

//方法三：
//        PageHelper.startPage(1,10);
//        List<ImportData> list = importDataMapper.query(importData);
//        PageInfo<ImportData> info = new PageInfo<>(list);

//        log.info("query.info.result:{}",info);
//        return info;
//        //pageHelper分页失败：todo：查询sql打印日志查看有没有加上limit
//        PageHelper.startPage(1,10);
//        List<ImportData> list = importDataMapper.query(importData);
//        PageInfo<ImportData> info = new PageInfo<>(list);
//        log.info("query.info.result:{}",info);
//        return info;

//方法四：mybatis-plus
//        Page<ImportData> page = new Page<>(1,10);
//        IPage<ImportData> iPage = importDataMapper.query(page);
//        PageUtils pageUtils = new PageUtils(iPage);
//        System.out.println("pageutils:"+pageUtils);
//        return pageUtils;

//错误方法：（结果中pagesize为10，size为总的，这是正确的，但是list的不应该是总条数，应该是10条，其他参数也是错的
// 例如第一页的num，和最后一页的num都不应该是1，也就是说不应该只有一页）
//        Page<ImportData> page = new Page<>(1,10);
//        List<ImportData> list = importDataMapper.query(importData);
//        page.addAll(list);
//        System.out.println("page:"+page);
//        PageInfo<ImportData> info = page.toPageInfo();
//        System.out.println("info"+info);
//
//        log.info("query.info.result:{}",info);
//        return info;
