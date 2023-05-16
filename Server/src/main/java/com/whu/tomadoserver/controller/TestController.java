package com.whu.tomadoserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 孔德昱
 * @date 2023/5/16 18:03 星期二
 */
@RestController
public class TestController {
    @GetMapping("/hello")
    public String hello(){
        return "Hello Client";
    }
}
