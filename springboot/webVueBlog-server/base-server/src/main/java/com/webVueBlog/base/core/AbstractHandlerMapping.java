package com.webVueBlog.base.core;

import com.webVueBlog.base.core.annotation.Async;
import com.webVueBlog.base.core.annotation.AsyncBatch;
import com.webVueBlog.base.core.annotation.PakMapping;
import com.webVueBlog.base.core.hanler.AsyncBatchHandler;
import com.webVueBlog.base.core.hanler.BaseHandler;
import com.webVueBlog.base.core.hanler.SyncHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息处理映射
 *
 * 
 */
public abstract class AbstractHandlerMapping implements HandlerMapping {

    private final Map<Object, BaseHandler> handlerMap = new HashMap<>(64);

    /**
     * 将node中被@Column标记的方法注册到映射表
     */
    protected synchronized void registerHandlers(Object bean) {
        Class<?> beanClass = bean.getClass();
        Method[] methods = beanClass.getDeclaredMethods();

        for (Method method : methods) {
            PakMapping annotation = method.getAnnotation(PakMapping.class);
            if (annotation != null) {

                String desc = annotation.desc();
                int[] types = annotation.types();

                AsyncBatch asyncBatch = method.getAnnotation(AsyncBatch.class);
                BaseHandler baseHandler;
                // 异步处理
                if (asyncBatch != null) {
                    baseHandler = new AsyncBatchHandler(bean, method, desc, asyncBatch.poolSize(), asyncBatch.maxMessageSize(), asyncBatch.maxWaitTime());
                } else {
                    baseHandler = new SyncHandler(bean, method, desc, method.isAnnotationPresent(Async.class));
                }

                for (int type : types) {
                    handlerMap.put(type, baseHandler);
                }

            }
        }
    }

    /**
     * 根据消息类型获取handler
     */
    @Override
    public BaseHandler getHandler(int messageId) {
        return handlerMap.get(messageId);
    }
}
