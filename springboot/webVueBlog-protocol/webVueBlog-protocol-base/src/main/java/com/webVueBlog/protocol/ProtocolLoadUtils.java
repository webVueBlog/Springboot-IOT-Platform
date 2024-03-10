package com.webVueBlog.protocol;

import com.webVueBlog.protocol.base.annotation.Column;
import com.webVueBlog.protocol.base.annotation.Columns;
import com.webVueBlog.protocol.base.annotation.MergeSubClass;
import com.webVueBlog.protocol.base.model.ActiveModel;
import com.webVueBlog.protocol.base.model.ModelRegistry;
import com.webVueBlog.protocol.base.model.WModel;
import com.webVueBlog.protocol.base.struc.BaseStructure;
import com.webVueBlog.protocol.util.ArrayMap;
import com.webVueBlog.protocol.util.ClassUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 消息架构加载
 */
public class ProtocolLoadUtils {
    private static final Map<String, ArrayMap<ActiveModel>> CACHE = new WeakHashMap<>();//缓存

    public static ArrayMap<ActiveModel> getActiveMap(Class typeClass) {
        return getActiveMap(CACHE, typeClass);
    }

    public static ActiveModel getActiveMap(Class typeClass, int version) {
        ArrayMap<ActiveModel> schemaMap = getActiveMap(CACHE, typeClass);//获取缓存
        if (schemaMap == null) {
            return null;//缓存不存在
        }
        return schemaMap.getOrDefault(version);//获取指定版本
    }

    public static ArrayMap<ActiveModel> getActiveMap(Map<String, ArrayMap<ActiveModel>> root, final Class typeClass) {
        ArrayMap<ActiveModel> schemaMap = root.get(typeClass.getName());
        //不支持循环引用
        if (schemaMap != null) {
            return schemaMap;
        }

        List<Field> fs = findFields(typeClass);
        if (fs.isEmpty()) {
            return null;
        }

        root.put(typeClass.getName(), schemaMap = new ArrayMap<>());//缓存

        Map<Integer, Set<BaseStructure>> multiVersionFields = findMultiVersionFields(root, fs);//获取多版本字段
        Set<BaseStructure> defFields = multiVersionFields.get(Integer.MAX_VALUE);//获取默认字段
        for (Map.Entry<Integer, Set<BaseStructure>> entry : multiVersionFields.entrySet()) {//遍历多版本字段

            Integer version = entry.getKey();//版本号
            Set<BaseStructure> fieldList = entry.getValue();//字段列表
            if (defFields != null && !version.equals(Integer.MAX_VALUE)) {//默认字段不为空
                for (BaseStructure defField : defFields) {
                    if (!fieldList.contains(defField)) {
                        fieldList.add(defField);
                    }
                }
            }

            BaseStructure[] fields = fieldList.toArray(new BaseStructure[fieldList.size()]);
            Arrays.sort(fields);

            ActiveModel schema = new ActiveModel(typeClass, version, fields);
            schemaMap.put(version, schema);
        }
        root.put(typeClass.getName(), schemaMap.fillDefaultValue());
        return schemaMap;
    }

    private static List<Field> findFields(Class typeClass) {
        LinkedList<Field> fs = new LinkedList<>();

        boolean addFirst = false;
        Class<?> temp = typeClass;

        while (temp != null) {
            if (addFirst) {
                fs.addAll(0, Arrays.asList(temp.getDeclaredFields()));
            } else {
                fs.addAll(Arrays.asList(temp.getDeclaredFields()));
            }
            MergeSubClass marge = temp.getAnnotation(MergeSubClass.class);
            if (marge == null) {
                break;
            }
            addFirst = marge.addBefore();
            temp = typeClass.getSuperclass();
        }

        List<Field> result = new ArrayList<>(fs.size());
        for (Field f : fs) {
            if (f.isAnnotationPresent(Columns.class) || f.isAnnotationPresent(Column.class)) {
                f.setAccessible(true);
                result.add(f);
            }
        }
        return result;
    }

    private static Map<Integer, Set<BaseStructure>> findMultiVersionFields(Map<String, ArrayMap<ActiveModel>> root, List<Field> fs) {
        final int size = fs.size();
        Map<Integer, Set<BaseStructure>> multiVersionFields = new TreeMap<Integer, Set<BaseStructure>>() {
            @Override
            public Set<BaseStructure> get(Object key) {
                Set result = super.get(key);
                if (result == null) {
                    super.put((Integer) key, result = new HashSet(size));
                }
                return result;
            }
        };

        for (int i = 0; i < size; i++) {
            Field f = fs.get(i);

            Column column = f.getDeclaredAnnotation(Column.class);
            if (column != null) {
                fillField(root, multiVersionFields, column, f, i);
            } else {
                Column[] clos = f.getDeclaredAnnotation(Columns.class).value();
                for (int j = 0; j < clos.length; j++) {
                    fillField(root, multiVersionFields, clos[j], f, i);
                }
            }
        }
        return multiVersionFields;
    }

    private static void fillField(Map<String, ArrayMap<ActiveModel>> root, Map<Integer, Set<BaseStructure>> multiVersionFields, Column column, Field field, int position) {
        BaseStructure BaseStructure = ModelRegistry.get(column, field);
        int[] versions = getVersions(column, ALL);
        if (BaseStructure != null) {
            for (int ver : versions) {
                multiVersionFields.get(ver).add(BaseStructure.init(column, field, position));
            }
        } else {
            ArrayMap<ActiveModel> modelMap = getActiveMap(root, ClassUtils.getGenericType(field));
            if (versions == ALL) {
                versions = modelMap.keys();
            }
            for (int ver : versions) {
                WModel model = modelMap.getOrDefault(ver);
                BaseStructure = ModelRegistry.get(column, field, model);
                multiVersionFields.get(ver).add(BaseStructure.init(column, field, position));
            }
        }
    }

    private static final int[] ALL = {Integer.MAX_VALUE};

    private static int[] getVersions(Column column, int[] def) {
        int[] result = column.version();
        if (result.length == 0) {
            result = def;
        }
        return result;
    }
}
