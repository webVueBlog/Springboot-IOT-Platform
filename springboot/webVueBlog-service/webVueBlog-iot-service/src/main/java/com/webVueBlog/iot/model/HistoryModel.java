package com.webVueBlog.iot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 
 */
@Data
public class HistoryModel {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    private String value;

    private String identity;
}
