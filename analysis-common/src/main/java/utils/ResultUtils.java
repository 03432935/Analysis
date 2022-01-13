package utils;

import java.util.HashMap;
import java.util.Map;

public class ResultUtils {
    final static String CODE = "code";
    final static String SUCCESS = "success";
    final static String FAIL = "fail";
    final static String MESSAGE = "message";

    /**
     * 返回数据
     */
    public static String result(Object code,Object message){
        Map<Object,Object> map = new HashMap<>();
        map.put(CODE,code);
        map.put(MESSAGE,message);
        return JsonUtils.objectToJson(map);
    }

    /**
     * success message
     * @param message
     * @return
     */
    public static String successResult(Object message){
        return ResultUtils.result(SUCCESS,message);
    }

    /**
     * fail message
     * @param message
     * @return
     */
    public static String failResult(Object message){
        return ResultUtils.result(FAIL,message);
    }
}
