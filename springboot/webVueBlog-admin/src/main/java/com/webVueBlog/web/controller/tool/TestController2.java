package com.webVueBlog.web.controller.tool;

import com.webVueBlog.common.annotation.Anonymous;
import com.webVueBlog.iot.mapper.DeviceMapper;
import com.webVueBlog.iot.model.DeviceRelateAlertLogVO;
import com.webVueBlog.iot.service.IDeviceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 测试类
 * 
 */
@Anonymous
@RestController
@RequestMapping("/test2")
public class TestController2 {

    @Resource
    private IDeviceService deviceService;
    @Resource
    private DeviceMapper deviceMapper;

    @GetMapping("/add")
    public void add()
    {
        Set<String> deviceNumbers = new HashSet<>();
        deviceNumbers.add("D1PGLPG58K88");
        deviceNumbers.add("D1F0L7P84D8Z");
        deviceNumbers.add("D1F0L7P84D8Z_2");
        List<DeviceRelateAlertLogVO> deviceRelateAlertLogVOList = deviceMapper.selectDeviceBySerialNumbers(deviceNumbers);
        Map<String, DeviceRelateAlertLogVO> deviceRelateAlertLogVOMap = deviceRelateAlertLogVOList.stream().collect(Collectors.toMap(DeviceRelateAlertLogVO::getSerialNumber, Function.identity()));
    }
}
