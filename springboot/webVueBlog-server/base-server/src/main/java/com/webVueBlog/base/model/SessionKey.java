package com.webVueBlog.base.model;

import com.webVueBlog.base.session.Session;

/**
 * 
 */
public enum SessionKey {

    DeviceMsg;

    public static DeviceMsg getDeviceMsg(Session session){
       return (DeviceMsg)session.getAttribute(SessionKey.DeviceMsg);
    }
}
