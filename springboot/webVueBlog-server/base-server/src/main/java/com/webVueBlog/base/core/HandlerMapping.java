package com.webVueBlog.base.core;

import com.webVueBlog.base.core.hanler.BaseHandler;

/**
 * 消息处理接口
 * 
 */
public interface HandlerMapping {

    BaseHandler getHandler(int messageId);
}
