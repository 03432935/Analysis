package com.analysis.service.service;

import com.analysis.dao.entity.EchartDto;
import com.analysis.dao.entity.ImportDto;

import java.util.List;

/**
 * @description:异常判断服务
 * @author: lingwanxian
 * @date: 2022/5/7 14:54
 */
public interface AbnormalJudgeService {

    /**
     # 正态分布
     # 3sigma准则 --->
     #  mean() - 3* std() ---下限
     #  mean() + 3* std() ---上限
     * @param data
     * @return
     */
    public List<EchartDto> threeSigmaFunction(List<ImportDto> data) throws Exception;

    public List<EchartDto> grubbsTestFunction(List<ImportDto> data) throws Exception;

    public List<EchartDto> LOFFunction(List<ImportDto> data) throws Exception;

    public List<EchartDto> iForestFunction(List<ImportDto> data) throws Exception;

    public List<EchartDto> holtWintersFunction(List<ImportDto> data) throws Exception;

    public List<EchartDto> ARMAFunction(List<ImportDto> data) throws Exception;




    /**
     * 实现指数移动平均值计算
     * @param data
     * @return
     */
    public List<ImportDto> EWMAFunction(List<ImportDto> data);

    /**
     * 多项式模型(暂时搁置
     * @param data
     * @return
     */
    public List<ImportDto> polynomialFunction(List<ImportDto> data);

    public List<ImportDto> XGBoostFunction(List<ImportDto> data);
}
