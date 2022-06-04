package com.analysis.service.service.impl;

import com.analysis.dao.entity.AvgDto;
import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;
import com.analysis.dao.mapper.ImportDtoMapper;
import com.analysis.service.enums.AbnormalDataEnum;
import com.analysis.service.enums.PredictionStrategyEnum;
import com.analysis.service.service.AbnormalJudgeService;
import com.analysis.service.service.ConversionParamService;
import com.analysis.service.service.StrategyService;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ARIMA.ARIMAImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ESD.GrubbsImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ExponentialMoving.HoltWintersImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.IsolationTree.IsolationTreeImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.LOF.LOFDetectImpl;
import com.analysis.service.service.impl.AnomalyDetectServiceImpl.ThreeSigma.ThreeSigmaImpl;
import com.analysis.service.utils.DisplayTool;
import com.analysis.service.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/5/7 14:56
 */
@Service
@Slf4j
public class AbnormalJudgeServiceImpl implements AbnormalJudgeService {

    @Autowired
    private ImportDtoMapper importDtoMapper;

    @Autowired
    private ConversionParamService conversionParamService;

    @Autowired
    private StrategyService strategyService;

    @Override
    public List<EchartDto> threeSigmaFunction(List<ImportDto> importDtoList) throws Exception {
        double[] data = importDtoList.stream().mapToDouble(ImportDto::getVData).toArray();
        ThreeSigmaImpl threeSigmaImpl = new ThreeSigmaImpl();
        log.info("begin three-sigma,input:{}",data);
        threeSigmaImpl.timeSeriesAnalyse(data);
        List<Result> temp = threeSigmaImpl.getResults();
        log.info("end three-sigma,output:{}",temp);
        List<ImportDto> res = new ArrayList<>();
        for (Result t: temp){
            ImportDto importDto = importDtoList.get(t.getIndex());
            res.add(importDto);
        }
        List<EchartDto> echartDtos = conversionParamService.importListToEchartDto(res);
        System.out.println(echartDtos);
        return echartDtos;

//        List<ImportDto> res = new ArrayList<>();
//        //平均值
//        Double avgValue = data.stream().mapToDouble(ImportDto::getVData).average().orElse(0.0);
//        //标准差
//        Double standardValue = standardVariance(data,avgValue);
//        for (ImportDto d : data){
//            if(Math.abs(d.getVData()-avgValue)>(3*standardValue)) {
//                res.add(d);
//                System.out.println("该数组中的"+d+"元素在3-sigma准则中属于异常值");
//            }
//        }
//        return res;
    }

    @Override
    public List<EchartDto> grubbsTestFunction(List<ImportDto> importDtoList) throws Exception {
        double[] data = importDtoList.stream().mapToDouble(ImportDto::getVData).toArray();
        GrubbsImpl grubbs = new GrubbsImpl();
        log.info("begin grubbs,input:{}",data);
        grubbs.timeSeriesAnalyse(data);
        List<Result> temp = grubbs.getResults();
        log.info("end grubbs,output:{}",temp);
        List<ImportDto> res = new ArrayList<>();
        for (Result t: temp){
            ImportDto importDto = importDtoList.get(t.getIndex());
            res.add(importDto);
        }
        List<EchartDto> echartDtos = conversionParamService.importListToEchartDto(res);
        System.out.println(echartDtos);
        return echartDtos;
    }

    @Override
    public List<EchartDto> LOFFunction(List<ImportDto> importDtoList) throws Exception {
        double[] data = importDtoList.stream().mapToDouble(ImportDto::getVData).toArray();
        LOFDetectImpl lofDetect = new LOFDetectImpl(300,10);
        log.info("begin lof,input:{}",data);
        lofDetect.timeSeriesAnalyse(data);
        List<Result> temp = lofDetect.getResults();
        log.info("end lof,output:{}",temp);
        List<ImportDto> res = new ArrayList<>();
        for (Result t: temp){
            ImportDto importDto = importDtoList.get(t.getIndex());
            res.add(importDto);
        }
        List<EchartDto> echartDtos = conversionParamService.importListToEchartDto(res);
        System.out.println(echartDtos);
        return echartDtos;
    }


    @Override
    public List<EchartDto> iForestFunction(List<ImportDto> importDtoList) throws Exception {
        double[] data = importDtoList.stream().mapToDouble(ImportDto::getVData).toArray();
        IsolationTreeImpl isolationTree = new IsolationTreeImpl();
        log.info("begin iForest,input:{}",data);
        isolationTree.timeSeriesAnalyse(data);
        List<Result> temp = isolationTree.getResults();
        log.info("end iForest,output:{}",temp);
        List<ImportDto> res = new ArrayList<>();
        for (Result t: temp){
            ImportDto importDto = importDtoList.get(t.getIndex());
            res.add(importDto);
        }
        List<EchartDto> echartDtos = conversionParamService.importListToEchartDto(res);
        System.out.println(echartDtos);
        return echartDtos;
    }

    @Override
    public List<EchartDto> holtWintersFunction(List<ImportDto> importDtoList) throws Exception {
        double[] data = importDtoList.stream().mapToDouble(ImportDto::getVData).toArray();
        HoltWintersImpl holtWinters = new HoltWintersImpl(0.1,0.01,1000);
        log.info("begin holtWinters,input:{}",data);
        holtWinters.timeSeriesAnalyse(data);
        List<Result> temp = holtWinters.getResults();
        log.info("end holtWinters,output:{}",temp);
        List<ImportDto> res = new ArrayList<>();
        for (Result t: temp){
            ImportDto importDto = importDtoList.get(t.getIndex());
            res.add(importDto);
        }
        List<EchartDto> echartDtos = conversionParamService.importListToEchartDto(res);
        System.out.println(echartDtos);
        return echartDtos;
    }

    @Override
    public List<EchartDto> ARMAFunction(List<ImportDto> importDtoList) throws Exception {
        double[] data = importDtoList.stream().mapToDouble(ImportDto::getVData).toArray();
        ARIMAImpl arima = new ARIMAImpl();
        log.info("begin arima,input:{}",importDtoList);
        arima.timeSeriesAnalyse(data);
        List<Result> temp = arima.getResults();
        log.info("end arima,output:{}",temp);
        List<ImportDto> res = new ArrayList<>();
        for (Result t: temp){
            ImportDto importDto = importDtoList.get(t.getIndex());
            res.add(importDto);
        }
        List<EchartDto> echartDtos = conversionParamService.importListToEchartDto(res);
        System.out.println(echartDtos);
        return echartDtos;
    }

    @Override
    public List<ImportDto> EWMAFunction(List<ImportDto> importDtoList) {
        return null;
    }

    @Override
    public List<ImportDto> polynomialFunction(List<ImportDto> importDtoList) {
        return null;
    }

    @Override
    public List<ImportDto> XGBoostFunction(List<ImportDto> importDtoList) {
        return null;
    }

    /**
     * 求标准差
     * @param data
     * @return
     */
    private Double standardVariance(List<ImportDto> data,Double avgValue){
        double total = 0.0;
        for (ImportDto d : data){
            total += Math.pow(d.getVData()-avgValue,2);
        }
        return Math.sqrt(total/(data.size()-1));
    }

}
