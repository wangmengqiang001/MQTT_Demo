package com.testmock.springmock.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class WebController {

   // @ResponseBody
	//@Rest
    @RequestMapping("/home")
    public String home(){
        return "Hello, this is homepage";
        //"<div style='height:100%;line-height:100%;background-image:linear-gradient(45deg, #9F025E, #F9C929);color:white;border-radius:2px;'></div>";
    }

}
