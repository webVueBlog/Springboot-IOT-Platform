package com.webVueBlog.iot.service;

import com.webVueBlog.iot.model.login.AuthRequestWrap;

/**
 * AuthRequest简单工程类接口
 *
 * 
 */
public interface IAuthRequestFactory {

    AuthRequestWrap getAuthRequest(String source);
}
