package com.cz.middlevisual.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ViewController
 * @function [视图解析页面跳转]
 * @Author lcz
 * @Date 2021/09/09 09:15
 */
@RestController
@RequestMapping("/view")
@Api(value = "视图解析页面跳转")
public class ViewController {

    @GetMapping("/index")
    @ApiOperation(value = "菜单视图页面首页跳转",notes = "视图解析定位")
    public String index() {
        return "index";
    }
}
