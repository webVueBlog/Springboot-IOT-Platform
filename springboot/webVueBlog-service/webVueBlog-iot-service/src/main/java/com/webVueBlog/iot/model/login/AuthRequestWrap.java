package com.webVueBlog.iot.model.login;

import com.webVueBlog.iot.domain.SocialPlatform;
import me.zhyd.oauth.request.AuthRequest;

public class AuthRequestWrap {
    private AuthRequest authRequest;// 第三方登录请求

    private SocialPlatform socialPlatform;// 第三方登录平台

    public AuthRequest getAuthRequest() {
        return authRequest;
    }

    public void setAuthRequest(AuthRequest authRequest) {
        this.authRequest = authRequest;
    }

    public SocialPlatform getSocialPlatform() {
        return socialPlatform;
    }

    public void setSocialPlatform(SocialPlatform socialPlatform) {
        this.socialPlatform = socialPlatform;
    }
}
