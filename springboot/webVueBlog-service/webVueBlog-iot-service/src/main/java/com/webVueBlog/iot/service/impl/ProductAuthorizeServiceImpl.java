package com.webVueBlog.iot.service.impl;

import com.webVueBlog.common.constant.HttpStatus;
import com.webVueBlog.common.core.domain.entity.SysUser;
import com.webVueBlog.common.exception.ServiceException;
import com.webVueBlog.common.utils.DateUtils;
import com.webVueBlog.common.utils.StringUtils;
import com.webVueBlog.common.utils.uuid.IdUtils;
import com.webVueBlog.iot.domain.ProductAuthorize;
import com.webVueBlog.iot.mapper.ProductAuthorizeMapper;
import com.webVueBlog.iot.model.ProductAuthorizeVO;
import com.webVueBlog.iot.service.IProductAuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.webVueBlog.common.utils.SecurityUtils.getLoginUser;

/**
 * 产品授权码Service业务层处理
 *
 */
@Service
public class ProductAuthorizeServiceImpl implements IProductAuthorizeService {
    @Autowired
    private ProductAuthorizeMapper productAuthorizeMapper;

    /**
     * 查询产品授权码
     *
     * @param authorizeId 产品授权码主键
     * @return 产品授权码
     */
    @Override
    public ProductAuthorize selectProductAuthorizeByAuthorizeId(Long authorizeId) {
        return productAuthorizeMapper.selectProductAuthorizeByAuthorizeId(authorizeId);
    }

    /**
     * 查询产品授权码列表
     *
     * @param productAuthorize 产品授权码
     * @return 产品授权码
     */
    @Override
    public List<ProductAuthorize> selectProductAuthorizeList(ProductAuthorize productAuthorize) {
        return productAuthorizeMapper.selectProductAuthorizeList(productAuthorize);
    }

    /**
     * 新增产品授权码
     *
     * @param productAuthorize 产品授权码
     * @return 结果
     */
    @Override
    public int insertProductAuthorize(ProductAuthorize productAuthorize) {
        // 1=未使用，2=使用中
        productAuthorize.setStatus(1);
        productAuthorize.setCreateTime(DateUtils.getNowDate());
        return productAuthorizeMapper.insertProductAuthorize(productAuthorize);
    }

    /**
     * 修改产品授权码
     *
     * @param productAuthorize 产品授权码
     * @return 结果
     */
    @Override
    public int updateProductAuthorize(ProductAuthorize productAuthorize) {
        if(productAuthorize.getDeviceId()!=null && productAuthorize.getDeviceId()!=0){
            // 1=未使用，2=使用中
            productAuthorize.setStatus(2);
            productAuthorize.setUpdateTime(DateUtils.getNowDate());
        }
        return productAuthorizeMapper.updateProductAuthorize(productAuthorize);
    }

    /**
     * 批量删除产品授权码
     *
     * @param authorizeIds 需要删除的产品授权码主键
     * @return 结果
     */
    @Override
    public int deleteProductAuthorizeByAuthorizeIds(Long[] authorizeIds) {
        return productAuthorizeMapper.deleteProductAuthorizeByAuthorizeIds(authorizeIds);
    }

    /**
     * 删除产品授权码信息
     *
     * @param authorizeId 产品授权码主键
     * @return 结果
     */
    @Override
    public int deleteProductAuthorizeByAuthorizeId(Long authorizeId) {
        return productAuthorizeMapper.deleteProductAuthorizeByAuthorizeId(authorizeId);
    }

    /**
     * 根据数量批量新增产品授权码
     *
     * @param productAuthorizeVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addProductAuthorizeByNum(ProductAuthorizeVO productAuthorizeVO) {
        Long productId = productAuthorizeVO.getProductId();
        int createNum = productAuthorizeVO.getCreateNum();
        List<ProductAuthorize> list = new ArrayList<>(createNum);
        SysUser user = getLoginUser().getUser();
        for (int i = 0; i < createNum; i++) {
            ProductAuthorize authorize = new ProductAuthorize();
            // 1=未使用，2=使用中
            authorize.setStatus(1);
            authorize.setProductId(productId);
            authorize.setCreateBy(user.getUserName());
            authorize.setCreateTime(DateUtils.getNowDate());
            authorize.setAuthorizeCode(IdUtils.fastSimpleUUID().toUpperCase());
            list.add(authorize);
        }
        return productAuthorizeMapper.insertBatchAuthorize(list);
    }

    /**
     * 根据产品id查询产品授权码
     * @param productId 产品id
     * @return
     */
    @Override
    public List<ProductAuthorize> listByProductId(Long productId) {
        return productAuthorizeMapper.selectProductAuthorizeListByProductId(productId);
    }

}
