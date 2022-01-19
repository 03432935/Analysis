package com.analysis.common.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {

    public static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * 把对象转换成json
     */
    public static String objectToJson(Object object){
        try {
            String json = JSON.toJSONString(object);
            logger.info("把对象object:{}转换成json:{}",object,json);
            return json;
        }catch (Exception e){
            logger.error("对象object:{}转换成json失败,异常消息:{}",object,e);
            return null;
        }
    }

}
