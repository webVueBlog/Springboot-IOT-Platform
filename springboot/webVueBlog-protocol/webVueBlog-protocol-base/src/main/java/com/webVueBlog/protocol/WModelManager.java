package com.webVueBlog.protocol;

import com.webVueBlog.protocol.base.annotation.Protocol;
import com.webVueBlog.protocol.base.model.ActiveModel;
import com.webVueBlog.protocol.util.ArrayMap;
import com.webVueBlog.protocol.util.ClassUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息架构管理类
 *
 */
public class WModelManager {

    private final Map<Integer, ArrayMap<ActiveModel>> typeIdMapping;// id和消息的映射关系

    private final Map<String, ArrayMap<ActiveModel>> typeClassMapping;// 类型和消息的映射关系

    public WModelManager() {
        this(128);
    }

    public WModelManager(int initialCapacity) {
        this.typeIdMapping = new HashMap<>(initialCapacity);// 初始化
        this.typeClassMapping = new HashMap<>(initialCapacity);// 初始化
    }

    /**
     *   构造函数
     * @param basePackages
     */
    public WModelManager(String... basePackages) {// 初始化
        this(256, basePackages);// 扫描指定包下的类
    }

    /**
     *  构造函数
     * @param initialCapacity
     * @param basePackages
     */
    public WModelManager(int initialCapacity, String... basePackages) {
        this(initialCapacity);// 初始化
        for (String basePackage : basePackages) {// 扫描指定包下的类
            List<Class> types = ClassUtils.getClassList(basePackage);// 获取所有类
            for (Class<?> type : types) {// 遍历所有类
                Protocol protocol = type.getAnnotation(Protocol.class);// 获取类上的协议注解
                if (protocol != null) {// 判断类上是否有协议注解
                    int[] values = protocol.value();// 获取协议注解中的协议id数组
                    for (Integer typeId : values) {// 遍历协议注解中的协议id
                        loadRuntimeSchema(typeId, type);// 加载运行时协议
                    }
                }
            }
        }
    }

    public void loadRuntimeSchema(Integer typeId, Class typeClass) {
        ArrayMap<ActiveModel> schemaMap = ProtocolLoadUtils.getActiveMap(typeClassMapping, typeClass);
        if (schemaMap != null) {
            typeIdMapping.put(typeId, schemaMap);
        }
    }

    public <T> ActiveModel<T> getActiveMap(Class<T> typeClass, int version) {
        ArrayMap<ActiveModel> schemaMap = ProtocolLoadUtils.getActiveMap(typeClassMapping, typeClass);
        if (schemaMap == null) {
            return null;
        }
        return schemaMap.getOrDefault(version);
    }

    public ArrayMap<ActiveModel> getActiveMap(Class typeClass) {
        return ProtocolLoadUtils.getActiveMap(typeClassMapping, typeClass);
    }

    public ActiveModel getActiveMap(Integer typeId, int version) {
        ArrayMap<ActiveModel> schemaMap = typeIdMapping.get(typeId);
        if (schemaMap == null) {
            return null;
        }
        return schemaMap.getOrDefault(version);
    }

    public ArrayMap<ActiveModel> getActiveMap(Integer typeId) {
        return typeIdMapping.get(typeId);
    }
}