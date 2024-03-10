package com.webVueBlog.iot.model.ThingsModels;

import lombok.Data;

import java.util.List;

/**
 * 
 */
@Data
public class ThingsItems {

    private List<String> ids;

    private Long productId;

    private Integer slaveId;
}
