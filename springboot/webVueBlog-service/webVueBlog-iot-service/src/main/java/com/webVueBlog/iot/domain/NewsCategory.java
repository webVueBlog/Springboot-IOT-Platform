package com.webVueBlog.iot.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.webVueBlog.common.annotation.Excel;
import com.webVueBlog.common.core.domain.BaseEntity;

/**
 * 新闻分类对象 news_category
 * 
 *
 */
@ApiModel(value = "NewsCategory", description = "新闻分类对象 news_category")
public class NewsCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    @ApiModelProperty("分类ID")
    @Excel(name = "分类ID")
    private Long categoryId;

    /** 分类名字 */
    @ApiModelProperty("分类名字")
    @Excel(name = "分类名字")
    private String categoryName;

    /** 显示顺序 */
    @ApiModelProperty("显示顺序")
    @Excel(name = "显示顺序")
    private Integer orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    @ApiModelProperty("删除标志")
    private String delFlag;

    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }
    public void setCategoryName(String categoryName) 
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName() 
    {
        return categoryName;
    }
    public void setOrderNum(Integer orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum() 
    {
        return orderNum;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("categoryId", getCategoryId())
            .append("categoryName", getCategoryName())
            .append("orderNum", getOrderNum())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
