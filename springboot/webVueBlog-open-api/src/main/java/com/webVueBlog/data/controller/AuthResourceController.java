package com.webVueBlog.data.controller;

import com.webVueBlog.common.core.controller.BaseController;
import org.springframework.web.bind.annotation.*;

/**
 * 设备告警Controller
 * 
 * 
 * 
 */
@RestController
@RequestMapping("/oauth/resource")
public class AuthResourceController extends BaseController
{
    /**
     * 查询设备告警列表
     */
    @GetMapping("/product")
    public String findAll() {
        return "查询产品列表成功！";
    }


}
