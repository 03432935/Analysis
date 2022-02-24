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

    public static Object jsonStringToObject(String json){
        try{
            Object o = JSON.parseObject(json);
            logger.info("把json字符串:{}转换成json对象:{}",json,o);
            return o;
        }catch (Exception e){
            logger.error("json字符串:{}转换成json对象失败，异常消息:{}",json,e);
            return null;
        }
    }

}
