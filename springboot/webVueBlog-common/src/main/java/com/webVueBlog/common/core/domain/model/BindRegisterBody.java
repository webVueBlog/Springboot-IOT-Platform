package com.webVueBlog.common.core.domain.model;

/**
 * 用户注册对象
 *
 *
 */
public class BindRegisterBody extends RegisterBody {
    /**
     * 绑定id
     */
    private String bindId;

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }
}
