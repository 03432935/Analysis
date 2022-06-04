package com.analysis.service.service.impl;

import com.analysis.common.utils.DateUtils;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.enums.AbnormalDataEnum;
import com.analysis.service.enums.CompletionStrategyEnum;
import com.analysis.service.factory.StrategyFactory;
import com.analysis.service.handler.AbstractStrategy;
import com.analysis.service.handler.AvgCompletionHandler;
import com.analysis.service.service.CompletionService;
import com.analysis.service.service.ConversionParamService;
import com.analysis.service.service.EchartsSendService;
import com.analysis.service.service.StrategyService;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ARIMA.ARIMAImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ExponentialMoving.HoltWintersImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.RandomForest.RandomForest;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.RandomForest.RandomForestImpl;
import com.analysis.service.utils.DisplayTool;
import com.analysis.service.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/16 22:45
 */
@Service
public class CompletionServiceImpl implements CompletionService {

    @Autowired
    private EchartsSendService echartsSendService;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private ImportDtoMapper importDtoMapper;

    @Autowired
    private ConversionParamService conversionParamService;

    @Override
    public List<EchartDto> strategyAvgFunction(ImportDto importDto) throws Exception {
        List<ImportDto> importDtoList = searchForNonData(importDto);

        List<ImportDto> importDtos = importDtoMapper.query(importDto);

        for (ImportDto dto : importDtoList){
            if (dto.getVData() == null){
//                UpdateWrapper<ImportDto> updateWrapper = new UpdateWrapper<>();
//                updateWrapper.set("completion_strategy",CompletionStrategyEnum.AVGSTRATEGY.getCode());
//                importDtoMapper.update(dto,updateWrapper);
//                QueryWrapper<ImportDto> queryWrapper = new QueryWrapper<>();
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(dto.getTTime());
//                cal.add(Calendar.DAY_OF_WEEK, -4);//取x周内的平均值
//                Date start = cal.getTime();
//                cal.add(Calendar.DAY_OF_WEEK, 4);
//                Date end = cal.getTime();
//                queryWrapper.eq("sen_id",importDto.getSenId());
//                queryWrapper.isNotNull("v_data");
//                queryWrapper.eq("abnormal", AbnormalDataEnum.NORMAL.getCode());
//                queryWrapper.between("t_time",start,end);
//                List<ImportDto> list = importDtoMapper.selectList(queryWrapper);

                //月均
                Double avgMonth = importDtos.stream().filter(
                        importDto1 ->
                                DateUtils.dateTimeToStrMonth(dto.getTTime()).equals(DateUtils.dateTimeToStrMonth(importDto1.getTTime()))
                ).mapToDouble(ImportDto::getVData).average().orElse(0.0);
                Calendar cal = Calendar.getInstance();
                cal.setTime(dto.getTTime());
                cal.add(Calendar.MONTH, -1);
                Date start = cal.getTime();

                Double avgMonthBefore = importDtos.stream().filter(
                        importDto1 ->
                                DateUtils.dateTimeToStrMonth(start).equals(DateUtils.dateTimeToStrMonth(importDto1.getTTime()))
                ).mapToDouble(ImportDto::getVData).average().orElse(0.0);

                cal.add(Calendar.MONTH, 2);
                Date end = cal.getTime();
                Double avgMonthAfter = importDtos.stream().filter(
                        importDto1 ->
                                DateUtils.dateTimeToStrMonth(end).equals(DateUtils.dateTimeToStrMonth(importDto1.getTTime()))
                ).mapToDouble(ImportDto::getVData).average().orElse(0.0);

                //年均
                Double avgYear = importDtos.stream().filter(
                        importDto1 ->
                                DateUtils.dateTimeToStrYear(dto.getTTime()).equals(DateUtils.dateTimeToStrYear(importDto1.getTTime()))
                ).mapToDouble(ImportDto::getVData).average().orElse(0.0);

                if (avgMonth != 0.0){
                    dto.setVData(avgMonth);
                    System.out.println("dto:"+dto+",avgMonth:"+avgMonth);
                }else if (avgMonthBefore != 0.0 && avgMonthAfter != 0.0){
                    Double tempAvg = (avgMonthBefore+avgMonthAfter)/2;
                    dto.setVData(tempAvg);
                    System.out.println("dto:"+dto+",avgMonthAvg:"+tempAvg);
                }else if (avgMonthBefore != 0.0 ){
                    dto.setVData(avgMonthBefore);
                    System.out.println("dto:"+dto+",avgMonth:"+avgMonthBefore);
                }else if (avgMonthAfter != 0.0){
                    dto.setVData(avgMonthAfter);
                    System.out.println("dto:"+dto+",avgMonth:"+avgMonthAfter);
                }
                else if(avgYear != 0.0){
                    dto.setVData(avgYear);
                    System.out.println("dto:"+dto+",avgYear:"+avgYear);
                }else{
                    Double avgValue = importDtos.stream().mapToDouble(ImportDto::getVData).average().orElse(0.0);
                    dto.setVData(avgValue);
                    System.out.println("dto:"+dto+",avg:"+avgValue);
                }

            }
        }
        return conversionParamService.importListToEchartDto(importDtoList);
    }

    @Override
    public List<EchartDto> holtWintersCompletionFunction(ImportDto importDto) throws Exception {
        List<ImportDto> importDtoList = searchForNonData(importDto);
        int[] nh = new int[importDtoList.size()/2];
        int j = 0;
        for (int i = 0; i < importDtoList.size(); i++) {
            if (importDtoList.get(i).getVData() == null){
                nh[j] = i;
                j++;
                importDtoList.get(i).setVData(7.0);
            }
        }
        double[] data = importDtoList.stream().mapToDouble(ImportDto::getVData).toArray();
        HoltWintersImpl holtWinters = new HoltWintersImpl(0.1,0.01,1000);
        double[] result = holtWinters.forCompletion(data);
        for (int i = 0; i < importDtoList.size()/2; i++) {
            System.out.println(result[nh[i]]);
            importDtoList.get(nh[i]).setVData(result[nh[i]]);
//            importDtoList.get(nh[i]).setCompletionStrategy("1");
            System.out.println(importDtoList.get(nh[i]));
        }

        List<EchartDto> echartDtos = conversionParamService.importListToEchartDto(importDtoList);

        return echartDtos;
    }

    @Override
    public List<EchartDto> armaCompletionFunction(ImportDto importDto) throws Exception {
        List<ImportDto> importDtoList = searchForNonData(importDto);
        double flag = 0.0;
        int[] nh = new int[importDtoList.size()/2];
        int j = 0;
        for (int i = 0; i < importDtoList.size(); i++) {
            if (importDtoList.get(i).getVData() == null){
                nh[j] = i;
                j++;
                importDtoList.get(i).setVData(flag);
//                importDtoList.get(i).setVData(importDtoList.get(i-1).getVData());
            }
        }
        double[] data = importDtoList.stream().mapToDouble(ImportDto::getVData).toArray();
        ARIMAImpl arima = new ARIMAImpl();
        arima.timeSeriesAnalyse(data);
        List<Result> res = arima.getResults();
        List<Double> predict = arima.getPredictData();
        int a = 0 ;
        for (Result result:res){
            if (result.getValue() == flag){
                importDtoList.get(result.getIndex()).setVData(predict.get(a));
                a++;
                System.out.println(importDtoList.get(result.getIndex()));
            }
        }
        List<EchartDto> echartDtos = conversionParamService.importListToEchartDto(importDtoList);
        return echartDtos;
    }


    /**
     * 随机森林
     * @param importDto 入参是查询条件
     * @return
     */
    @Override
    public List<EchartDto> randomForestFunction(ImportDto importDto) {
        List<ImportDto> importDtoList = searchForNonData(importDto);
        RandomForest rf = new RandomForest();
        double[][] instances = new double[1][];
        int[] targets = new int[importDtoList.size()];
        for (int i = 0; i < importDtoList.size(); i++) {
            if ("0".equals(importDtoList.get(i).getCompletionStrategy())){
                //正常
                targets[i] = 0;
            }else if ("1".equals(importDtoList.get(i).getCompletionStrategy())){
                //异常缺失
                targets[i] = 1;
            }
        }
        double[] data = importDtoList.stream().mapToDouble(ImportDto::getVData).toArray();
        RandomForestImpl randomForest = new RandomForestImpl();
        randomForest.timeSeriesAnalyse(data);

//        List<EchartDto> echartDtos = conversionParamService.importListToEchartDto(res);

        return null;
    }

    //先查该时间应该有的数据，一整条都没有
    private List<ImportDto> searchForNonData(ImportDto importDto){
        int needComp = 0;
        //根据查询条件拿到数据
        List<ImportDto> importDtoList = echartsSendService.beforeAlgorithm(importDto);
        //检查数据之间缺失的
        for (int i = 0; i < importDtoList.size()-1; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(importDtoList.get(i).getTTime());
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            if(hour != 6 && hour!= 18){
                continue;
            }
            cal.add(Calendar.HOUR, 12);//往后推12小时看看有没有数据
            Date tempData = cal.getTime();

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(importDtoList.get(i+1).getTTime());
            int hours = cal2.get(Calendar.HOUR_OF_DAY);
            if(hours != 6 && hours!= 18){
                continue;
            }
            if(tempData.equals(importDtoList.get(i+1).getTTime())){
                continue;
            }else{
                System.out.println("缺时间为"+tempData+"的数据");
                ImportDto importDto1 = new ImportDto();
                importDto1.setTTime(tempData);
                importDto1.setSenId(importDto.getSenId());
                importDto1.setCompletionStrategy("1");
                importDtoList.add(i+1,importDto1);
                needComp++;
            }
        }
        System.out.println(needComp);
        return importDtoList;
    }
}
