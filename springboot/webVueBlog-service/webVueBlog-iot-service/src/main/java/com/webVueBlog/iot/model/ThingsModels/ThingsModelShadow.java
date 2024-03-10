package com.webVueBlog.iot.model.ThingsModels;

import com.webVueBlog.common.core.thingsModel.ThingsModelSimpleItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品分类的Id和名称输出
 *
 * 
 * 
 */
public class ThingsModelShadow
{
    public ThingsModelShadow(){
        this.properties=new ArrayList<>();
        this.functions=new ArrayList<>();
    }

    public ThingsModelShadow(List<ThingsModelSimpleItem> properties, List<ThingsModelSimpleItem> functions){
        this.properties=properties;
        this.functions=functions;
    }

    /** 属性 */
    List<ThingsModelSimpleItem> properties;

    /** 功能 */
    List<ThingsModelSimpleItem> functions;

    public List<ThingsModelSimpleItem> getProperties() {
        return properties;
    }

    public void setProperties(List<ThingsModelSimpleItem> properties) {
        this.properties = properties;
    }

    public List<ThingsModelSimpleItem> getFunctions() {
        return functions;
    }

    public void setFunctions(List<ThingsModelSimpleItem> functions) {
        this.functions = functions;
    }
}
