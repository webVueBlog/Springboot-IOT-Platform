package com.webVueBlog.base.core;

import com.webVueBlog.base.core.annotation.Node;
import com.webVueBlog.base.util.ClassUtils;

import java.util.List;

/**
 * 默认消息映射处理类
 * 
 */
public class DefaultHandlerMapping extends AbstractHandlerMapping {

    public DefaultHandlerMapping(String endpointPackage) {
        List<Class> endpointClasses = ClassUtils.getClassList(endpointPackage, Node.class);

        for (Class endpointClass : endpointClasses) {
            try {
                Object bean = endpointClass.getDeclaredConstructor((Class[]) null).newInstance((Object[]) null);
                super.registerHandlers(bean);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
