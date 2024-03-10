package com.webVueBlog.base.core.hanler;

import com.webVueBlog.common.core.protocol.Message;
import com.webVueBlog.base.session.Session;


import java.lang.reflect.Method;

/**
 * 同步处理报文
 * 
 */
public class SyncHandler extends BaseHandler{

    public SyncHandler(Object target, Method targetMethod, String desc,boolean async) {
        super(target, targetMethod, desc, async);
    }

    @Override
    public <T extends Message> T invoke(T request, Session session) throws Exception {
        return super.invoke(request, session);
    }
}
