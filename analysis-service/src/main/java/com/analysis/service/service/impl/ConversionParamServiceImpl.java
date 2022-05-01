package com.analysis.service.service.impl;

import com.analysis.common.utils.DateUtils;
import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.entity.ImportKeyDto;
import com.analysis.service.enums.CompletionStrategyEnum;
import com.analysis.service.enums.PredictionStrategyEnum;
import com.analysis.service.service.BatchQueryAvgService;
import com.analysis.service.service.BatchQueryImportService;
import com.analysis.service.service.ConversionParamService;
import com.analysis.service.service.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/10 17:08
 */
@Slf4j
@Service
public class ConversionParamServiceImpl implements ConversionParamService {

    @Autowired
    private BatchQueryImportService batchQueryImportService;

    @Autowired
    private BatchQueryAvgService batchQueryAvgService;

    @Autowired
    private ConversionParamService conversionParamService;

    @Autowired
    private StrategyService strategyService;

    @Override
    public List<EchartDto> importListToEchartDto(List<ImportDto> list) throws Exception {
        try {
            List<EchartDto> echartDtos = list.stream()
                    .map(
                            importData -> {
                                EchartDto echartDto = new EchartDto();
                                echartDto.setTimeData(importData.getTTime());
                                echartDto.setVData(importData.getVData());
                                return echartDto;
                            }
                    ).sorted(Comparator.comparing(EchartDto::getTimeData))
                    .collect(Collectors.toList());
            log.info("getEchartData.echartDtos:" + echartDtos);
            return echartDtos;
        } catch (Exception e) {
            log.error("ImportDto转成EchartDto出错：ImportDto:{}", list);
            throw new Exception("ImportDto转成EchartDto出错：ImportDto:{}" + list, e);
        }
    }

    @Override
    public List<EchartDto> avgListToEchartDto(List<AvgDto> list) throws Exception {
        try {
            List<EchartDto> echartDtos = list.stream()
                    .map(
                            avgDto -> {
                                EchartDto echartDto = new EchartDto();
                                echartDto.setTimeData(avgDto.getTTime());
                                echartDto.setVData(avgDto.getVData());
                                return echartDto;
                            }
                    ).sorted(Comparator.comparing(EchartDto::getTimeData))
                    .collect(Collectors.toList());
            log.info("getEchartData.echartDtos:" + echartDtos);
            return echartDtos;
        } catch (Exception e) {
            log.error("AvgDto转成EchartDto出错：avgDto:{}", list);
            throw new Exception("AvgDto转成EchartDto出错：avgDto:{}" + list, e);
        }
    }

    @Override
    public List<ImportKeyDto> importListToImportKey(List<ImportDto> list) throws Exception {
        try {
            List<ImportKeyDto> keyDtos = list.stream()
                    .map(
                            importDto -> {
                                ImportKeyDto importKeyDto = new ImportKeyDto();
                                importKeyDto.setSenId(importDto.getSenId());
                                importKeyDto.setTimeData(importDto.getTTime());
                                importKeyDto.setVData(importDto.getVData());
                                return importKeyDto;
                            }
                    ).sorted(Comparator.comparing(ImportKeyDto::getTimeData))
                    .collect(Collectors.toList());
            log.info("getImportKeyDto.keyDtos:" + keyDtos);
            return keyDtos;
        } catch (Exception e) {
            log.error("ImportDto转成ImportKeyDto出错：ImportDto:{}", list);
            throw new Exception("ImportDto转成ImportKeyDto出错：ImportDto:{}" + list, e);
        }

    }

    @Override
    public List<EchartDto> resampling(List<ImportDto> list) throws Exception {

        List<EchartDto> echartDtos = conversionParamService.importListToEchartDto(list);
        List<EchartDto> res = new ArrayList<>();

        for (EchartDto echartDto:echartDtos){
            //日均
            Double avgDay = echartDtos.stream().filter(
                            echartDto1 ->
                                    DateUtils.dateTimeToStrDay(echartDto.getTimeData()).equals(DateUtils.dateTimeToStrDay(echartDto1.getTimeData())))
                    .mapToDouble(EchartDto::getVData).average().orElse(0.0);
            Date time = DateUtils.dateTimeToDateDay(echartDto.getTimeData());
            if (res.size()>0 && res.get(res.size()-1).getTimeData().equals(time)){
                continue;
            }
            EchartDto resDto = new EchartDto();
            resDto.setTimeData(DateUtils.dateTimeToDateDay(echartDto.getTimeData()));
            resDto.setVData(avgDay);
            res.add(resDto);
        }
        System.out.println(res);
        return res;
    }

    @Override
    public List<AvgDto> pyBackStringToAvg(String string,Date date) throws ParseException {
        if (string.isBlank()){
            log.error("python无输出，此处有问题");
            System.out.println("python输出有问题");
        }
        int start = string.indexOf("[");
        int end = string.indexOf("]");
        String subString = string.substring(start+1,end);
        System.out.println("substring"+subString);
        String[] splitString = subString.split(",");
        System.out.println("splitstring"+ Arrays.toString(splitString));

        List<AvgDto> avgDtos = new ArrayList<>();
        int i = 1;
        for (String s : splitString) {
            if (s.isBlank()){
                continue;
            }
            AvgDto dto = new AvgDto();
            //加上策略ARMA的标识
            dto.setStrategyCode(PredictionStrategyEnum.ARMA.getCode());
            System.out.println("s:"+s);
            Double d = Double.parseDouble(s.trim());

            Calendar calendar = new GregorianCalendar();
            string = DateUtils.getOneDayStartTime(date);
            date = DateUtils.strToDateTime(string);
            calendar.setTime(date);
            calendar.add(Calendar.DATE,i); //把日期往后增加一天,整数  往后推,负数往前移动
            date = calendar.getTime(); //这个时间就是日期往后推一天的结果
            dto.setVData(d);
            dto.setTTime(date);
            avgDtos.add(dto);
        }
        return avgDtos;
    }
}
