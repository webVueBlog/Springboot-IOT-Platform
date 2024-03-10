package com.webVueBlog.iot.service;

import com.webVueBlog.common.core.domain.AjaxResult;
import com.webVueBlog.iot.domain.Product;
import com.webVueBlog.iot.model.ChangeProductStatusModel;
import com.webVueBlog.iot.model.IdAndName;
import com.webVueBlog.iot.model.ProductCode;

import java.util.List;

/**
 * 产品Service接口
 *
 * 
 * 
 */
public interface IProductService
{
    /**
     * 查询产品
     *
     * @param productId 产品主键
     * @return 产品
     */
    public Product selectProductByProductId(Long productId);

    /**
     * 查询产品列表
     *
     * @param product 产品
     * @return 产品集合
     */
    public List<Product> selectProductList(Product product);

    /**
     * 查询产品简短列表
     *
     * @return 产品集合
     */
    public List<IdAndName> selectProductShortList();

    /**
     * 新增产品
     *
     * @param product 产品
     * @return 结果
     */
    public Product insertProduct(Product product);

    /**
     * 修改产品
     *
     * @param product 产品
     * @return 结果
     */
    public int updateProduct(Product product);

    /**
     * 获取产品下面的设备数量
     *
     * @param productId 产品ID
     * @return 结果
     */
    public int selectDeviceCountByProductId(Long productId);

    /**
     * 更新产品状态，1-未发布，2-已发布
     *
     * @param model
     * @return 结果
     */
    public AjaxResult changeProductStatus(ChangeProductStatusModel model);

    /**
     * 批量删除产品
     *
     * @param productIds 需要删除的产品主键集合
     * @return 结果
     */
    public AjaxResult deleteProductByProductIds(Long[] productIds);

    /**
     * 删除产品信息
     *
     * @param productId 产品主键
     * @return 结果
     */
    public int deleteProductByProductId(Long productId);

    /**
     * 根据设备编号查询产品信息
     * @param serialNumber 设备编号
     * @return 结果
     */
    public Product getProductBySerialNumber(String serialNumber);

    /**
     * 根据设备编号查询协议编号
     * @param serialNumber 设备编号
     * @return 协议编号
     */
    public ProductCode getProtocolBySerialNumber(String serialNumber);

    /**
     * 根据产品id获取协议编号
     * @param productId 产品id
     * @return 协议编号
     */
    public String getProtocolByProductId(Long productId);


    /**
     * 根据模板id查询所有使用的产品
     * @param templeId 模板id
     * @return
     */
    public List<Product> selectByTempleId(Long templeId);

}
