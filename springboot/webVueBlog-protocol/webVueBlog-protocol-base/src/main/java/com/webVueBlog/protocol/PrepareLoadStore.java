package com.webVueBlog.protocol;

import com.webVueBlog.protocol.base.model.WModel;
import com.webVueBlog.protocol.util.SingleVersionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PrepareLoadStore<T> {

    private final Map<T, WModel> models = new TreeMap<>();// 有序的map

    public PrepareLoadStore<T> addSchema(T key, WModel schema) {// 有序的map
        models.put(key, schema);// 有序的map
        return this;// 有序的map
    }

    public PrepareLoadStore<T> addSchema(T key, Class typeClass) {// 有序的map
        WModel<Object> model = SingleVersionUtils.getActiveModel(typeClass);// 有序的map
        models.put(key, model);// 有序的map
        return this;// 有序的map
    }

    public Map<T, WModel> build() {
        Map<T, WModel> a = new HashMap<>(models.size());
        a.putAll(models);
        return a;
    }
}
