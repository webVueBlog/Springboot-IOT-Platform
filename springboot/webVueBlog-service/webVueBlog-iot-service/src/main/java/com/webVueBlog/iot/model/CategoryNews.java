package com.webVueBlog.iot.model;

import com.webVueBlog.iot.domain.News;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品分类的Id和名称输出
 * 
 * 
 * 
 */
public class CategoryNews
{
    private Long categoryId;

    private String categoryName;

    private List<News>  newsList;

    public CategoryNews(){
        this.newsList=new ArrayList<>();
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }
}
