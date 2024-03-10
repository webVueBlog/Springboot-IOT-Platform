package com.webVueBlog.iot.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 动作
 *
 * 
 */
public class MonitorModel
{
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    private String value;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        }
}
