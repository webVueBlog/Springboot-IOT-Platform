package com.webVueBlog.iot.model;

/**
 * 批量新增产品授权码VO
 *
 * 
 */

public class ProductAuthorizeVO {

	private Long productId;// 产品ID
	private int createNum;// 授权码数量

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public int getCreateNum() {
		return createNum;
	}

	public void setCreateNum(int createNum) {
		this.createNum = createNum;
	}
}
