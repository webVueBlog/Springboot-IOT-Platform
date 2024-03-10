package com.webVueBlog.iot.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * id和name
 * 
 * 
 * 
 */
public class IdAndName
{
    private Long id;

    private String name;


    public void setId(Long id)
    {
        this.id = id;
    }
    public Long getId()
    {
        return id;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("categoryId", getId())
            .append("categoryName", getName())
            .toString();
    }
}
