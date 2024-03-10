package com.webVueBlog.common.core.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 拓展多租户的 BaseDO 基类
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TenantBaseDO extends BaseDO {

    /**
     * 多租户编号
     */
    private Long tenantId;

}
