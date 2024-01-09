package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping({"","/"})
    public String index(){
        //머스테치 사용. (스프링 권장사항) (기본경로 : src/main/resource/)
        //뷰 리졸버 설정 : templates(prefix), .mustache (suffix)
        return "index"; // src/main/resources/template/index.mustache
    }

}
