package com.webVueBlog.iot.domain;

import com.webVueBlog.iot.model.varTemp.DeviceSlavePoint;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.webVueBlog.common.annotation.Excel;
import com.webVueBlog.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 设备采集点模板关联对象 iot_device_template
 * 
 * domain是实体类在项目中的位置，一般根据项目的包名进行定义
 *
 */
@ApiModel(value = "DeviceTemplate", description = "设备采集点模板关联对象 iot_device_template")
public class DeviceTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 无意义自增ID */
    @ApiModelProperty("自增ID")
    private Long id;

    /** 产品id */
    @ApiModelProperty("产品id")
    @Excel(name = "产品id")
    private Long productId;

    /** 采集点模板id */
    @ApiModelProperty("采集点模板id")
    @Excel(name = "采集点模板id")
    private Long templateId;

    private List<DeviceSlavePoint> pointList;

    public List<DeviceSlavePoint> getPointList() {
        return pointList;
    }

    public void setPointList(List<DeviceSlavePoint> pointList) {
        this.pointList = pointList;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setTemplateId(Long templateId) 
    {
        this.templateId = templateId;
    }

    public Long getTemplateId() 
    {
        return templateId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productId", getProductId())
            .append("templateId", getTemplateId())
            .toString();
    }
}
