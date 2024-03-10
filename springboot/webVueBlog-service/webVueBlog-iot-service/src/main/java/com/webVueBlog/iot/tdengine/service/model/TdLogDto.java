package com.webVueBlog.iot.tdengine.service.model;

import com.webVueBlog.iot.domain.DeviceLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 
 */
@Data
@NoArgsConstructor//@NoArgsConstructor是lombok注解，用于生成一个无参构造函数。
@AllArgsConstructor//@AllArgsConstructor是lombok注解，用于生成一个包含所有参数的构造函数。
public class TdLogDto {

    /**
     * 设备编号
     */
    private String serialNumber;
    /**
     * 设备日志列表
     */
    private List<DeviceLog> list;
}
