package com.webVueBlog.iot.oauth.api;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpeakerController {// 假设这是speaker的controller
    @GetMapping("/oauth/speaker/get")
    public JSONObject getSpeaker() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JSONObject Json = new JSONObject();
        Json.put("1", "1");
        Json.put("2", "2");
        Json.put("3", "3");
        System.out.println("调用了接口get");
        return Json;
    }
    @PostMapping("/oauth/speaker/post")
    public JSONObject postSpeaker() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JSONObject bookJson = new JSONObject();
        bookJson.put("1", "1");
        System.out.println("调用了接口post");
        return bookJson;
    }
}
