package com.webVueBlog.common.utils.gateway.mq;

import lombok.Data;

/**
 * 
 */
@Data
public class TopicsPost {

    private String[] topics;
    private int[] qos;
}
