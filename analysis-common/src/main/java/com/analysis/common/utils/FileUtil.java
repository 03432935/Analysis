package com.analysis.common.utils;

import java.io.File;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/1/20 21:13
 */
public class FileUtil {

    private FileUtil(){
        throw new UnsupportedOperationException("you can't instantiate me...");
    }

    public static String getPath(File file){
        if (file == null){
            return null;
        }
        String filePath = file.getPath();
        return filePath;
    }
}
