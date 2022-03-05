package com.analysis.service.service.impl;

import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportData;
import com.analysis.dao.mapper.ImportDataMapper;
import com.analysis.service.service.BatchQueryImportService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/2/21 16:12
 */
@Service
@Slf4j
public class BatchQueryImportServiceImpl extends SuperServiceImpl<ImportDataMapper,ImportData> implements BatchQueryImportService {

    @Autowired
    private ImportDataMapper importDataMapper;

    @Autowired
    private BatchQueryImportService batchQueryImportService;

    @Override
    public IPage<ImportData> query(ImportData importData){

        QueryWrapper<ImportData> queryWrapper = new QueryWrapper<>();
        // 构建搜索条件
        queryWrapper.eq("sen_id","1110000201");
        Page<ImportData> page = new Page<>(importData.getCurrentPage(),importData.getPageSize());
//        IPage<ImportData> pageList = batchQueryImportService.page(page,queryWrapper); //两种表现形式
        IPage<ImportData> pageList = importDataMapper.selectPage(page,queryWrapper);
//        List<ImportData> list = pageList.getRecords();
        System.out.println("page:"+pageList);
        return pageList;

    }

    @Override
    public List<ImportData> getList() {
        List<ImportData> list = importDataMapper.selectList(null);
        log.info("BatchQueryImportServiceImpl.getList:"+list);
        return list;
    }

    @Override
    public List<EchartDto> getEchartData(List<ImportData> importDataList) {
        List<EchartDto> echartDtos = importDataList.stream()
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
