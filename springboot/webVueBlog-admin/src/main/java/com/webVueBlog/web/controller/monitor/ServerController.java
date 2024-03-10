package com.webVueBlog.web.controller.monitor;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.webVueBlog.common.core.domain.AjaxResult;
import com.webVueBlog.framework.web.domain.Server;

/**
 * 服务器监控
 * 
 *
 */
@Api(tags = "服务器监控")
@RestController
@RequestMapping("/monitor/server")
public class ServerController
{
    @ApiOperation("获取服务器信息")
    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception
    {
        Server server = new Server();
        server.copyTo();
        return AjaxResult.success(server);
    }
}
