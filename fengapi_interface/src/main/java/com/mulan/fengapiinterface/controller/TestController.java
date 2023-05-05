package com.mulan.fengapiinterface.controller;

import com.mulan.fengapiinterface.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/getText")
    public String getTest(String text) {
        return "get：" + text;
    }

    @PostMapping("/chat")
    public String postTest(@RequestBody String message) {
        return "post：" + message;
    }

    @PostMapping("/getUser")
    public String postBodyTest(@RequestBody User user, HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        System.out.println("成功");
        return "post：" + user.getUserName();
    }
}
