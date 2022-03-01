//package com.analysis.service.conf;
//
//import com.github.pagehelper.PageHelper;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Properties;
//
///**
// * @description:
// * @author: lingwanxian
// * @date: 2022/3/1 15:43
// */
//@Configuration
//public class PageHelperConfigure {
//    @Bean
//    public PageHelper pageHelper() {
//        PageHelper pageHelper = new PageHelper();
//        //添加配置，也可以指定文件路径
//        Properties p = new Properties();
//        p.setProperty("offsetAsPageNum", "true");
//        p.setProperty("rowBoundsWithCount", "true");
//        p.setProperty("reasonable", "true");
//        pageHelper.setProperties(p);
//        return pageHelper;
//    }
//}
//
