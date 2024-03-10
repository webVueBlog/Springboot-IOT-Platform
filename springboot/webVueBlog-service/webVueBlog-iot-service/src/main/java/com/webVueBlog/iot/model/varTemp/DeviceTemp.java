package com.webVueBlog.iot.model.varTemp;

import lombok.Data;

import java.util.List;

/**
 * 
 */
@Data
public class DeviceTemp {

    private Long productId;

    private String serialNumber;//设备序列号

    private List<DeviceSlavePoint> pointList;
}
