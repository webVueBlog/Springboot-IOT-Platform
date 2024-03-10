package com.webVueBlog.iot.model.varTemp;

import lombok.Data;

import java.util.List;

/**
 * 
 */
@Data
public class SyncModel {

    private List<Long> productIds;// 产品id集合
    private Long templateId;// 模板id
}
