package com.webVueBlog.iot.model;

/**
 * id和name
 * 
 * 
 * 
 */
public class ChangeProductStatusModel
{
    private Long productId;

    private Integer status;

    private Integer deviceType;

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
