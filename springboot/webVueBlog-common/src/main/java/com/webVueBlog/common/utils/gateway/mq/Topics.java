package com.webVueBlog.common.utils.gateway.mq;

import lombok.Data;

/**
 * 
 */
@Data
public class Topics {


    private String topicName;
    private Integer qos =0;
    private String desc;

}
