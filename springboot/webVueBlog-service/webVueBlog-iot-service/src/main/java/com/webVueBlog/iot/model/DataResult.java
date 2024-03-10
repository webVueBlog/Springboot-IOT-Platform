package com.webVueBlog.iot.model;

import lombok.Data;

import java.util.Date;

/**
 * 
 */
@Data
public class DataResult {

    private String id;
    /**值*/
    private String value;
    /**时间*/
    private Date  ts;
}
