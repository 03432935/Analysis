package com.geologic.hazard.analysis.analysis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
public class helloController {

    @RequestMapping("/world")
    @ResponseBody
    public String hello(){
        return "hello beauty";
    }
}
