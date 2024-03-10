package com.webVueBlog.data.controller;

import com.webVueBlog.common.annotation.Log;
import com.webVueBlog.common.core.controller.BaseController;
import com.webVueBlog.common.core.domain.AjaxResult;
import com.webVueBlog.common.core.page.TableDataInfo;
import com.webVueBlog.common.enums.BusinessType;
import com.webVueBlog.common.utils.StringUtils;
import com.webVueBlog.common.utils.poi.ExcelUtil;
import com.webVueBlog.iot.domain.ThingsModel;
import com.webVueBlog.iot.model.ImportThingsModelInput;
import com.webVueBlog.iot.model.ThingsModelPerm;
import com.webVueBlog.iot.model.varTemp.SyncModel;
import com.webVueBlog.iot.service.IThingsModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 物模型Controller
 * 
 *
 * 
 */
@RestController
@RequestMapping("/iot/model")
@Api(tags="产品物模型")
public class ThingsModelController extends BaseController
{
    @Autowired
    private IThingsModelService thingsModelService;

    /**
     * 查询物模型列表
     */
    @PreAuthorize("@ss.hasPermi('iot:device:list')")
    @GetMapping("/list")
    @ApiOperation("产品物模型分页列表")
    public TableDataInfo list(ThingsModel thingsModel)
    {
        startPage();
        List<ThingsModel> list = thingsModelService.selectThingsModelList(thingsModel);
        return getDataTable(list);
    }

    /**
     * 查询物模型对应设备分享权限
     */
    @PreAuthorize("@ss.hasPermi('iot:device:list')")
    @GetMapping("/permList/{productId}")
    @ApiOperation("查询物模型对应设备分享权限")
    public AjaxResult permList(@PathVariable Long productId)
    {
        List<ThingsModelPerm> list = thingsModelService.selectThingsModelPermList(productId);
        return AjaxResult.success(list);
    }

    /**
     * 获取物模型详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:device:query')")
    @GetMapping(value = "/{modelId}")
    @ApiOperation("获取产品物模型详情")
    public AjaxResult getInfo(@PathVariable("modelId") Long modelId)
    {
        return AjaxResult.success(thingsModelService.selectThingsModelByModelId(modelId));
    }

    /**
     * 新增物模型
     */
    @PreAuthorize("@ss.hasPermi('iot:device:add')")
    @Log(title = "物模型", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("添加产品物模型")
    public AjaxResult add(@RequestBody ThingsModel thingsModel)
    {
        int result=thingsModelService.insertThingsModel(thingsModel);
        if(result==1){
            return AjaxResult.success();
        }else if(result==2){
            return AjaxResult.error("产品下的标识符不能重复");
        }else{
            return AjaxResult.error();
        }
    }

    @Log(title = "导入物模型",businessType = BusinessType.INSERT)
    @PostMapping("/import")
    @ApiOperation("导入通用物模型")
    public AjaxResult ImportByTemplateIds(@RequestBody ImportThingsModelInput input){
        int repeatCount=thingsModelService.importByTemplateIds(input);
        if(repeatCount==0){
            return AjaxResult.success("数据导入成功");
        }else{
            return AjaxResult.success(repeatCount+"条数据未导入，标识符重复");
        }
    }

    /**
     * 修改物模型
     */
    @PreAuthorize("@ss.hasPermi('iot:device:edit')")
    @Log(title = "物模型", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改产品物模型")
    public AjaxResult edit(@RequestBody ThingsModel thingsModel)
    {
        int result=thingsModelService.updateThingsModel(thingsModel);
        if(result==1){
            return AjaxResult.success();
        }else if(result==2){
            return AjaxResult.error("产品下的标识符不能重复");
        }else{
            return AjaxResult.error();
        }
    }

    /**
     * 删除物模型
     */
    @PreAuthorize("@ss.hasPermi('iot:device:remove')")
    @Log(title = "物模型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{modelIds}")
    @ApiOperation("批量删除产品物模型")
    public AjaxResult remove(@PathVariable Long[] modelIds)
    {
        return toAjax(thingsModelService.deleteThingsModelByModelIds(modelIds));
    }

    /**
     * 获取缓存的JSON物模型
     */
    @PreAuthorize("@ss.hasPermi('iot:device:query')")
    @GetMapping(value = "/cache/{productId}")
    @ApiOperation("获取缓存的JSON物模型")
    public AjaxResult getCacheThingsModelByProductId(@PathVariable("productId") Long productId)
    {
        return AjaxResult.success("操作成功",thingsModelService.getCacheThingsModelByProductId(productId));
    }

    @ApiOperation(value = "物模型导入模板")
    @PostMapping("/temp")
    public void temp(HttpServletResponse response){
        ExcelUtil<ThingsModel> excelUtil = new ExcelUtil<>(ThingsModel.class);
        excelUtil.importTemplateExcel(response,"采集点");
    }


    /**
     * 导入采集点
     */
    @PreAuthorize("@ss.hasPermi('iot:point:import')")
    @ApiOperation(value = "采集点导入")
    @PostMapping(value = "/importData")
    public AjaxResult importData(MultipartFile file, Integer tempSlaveId) throws Exception{
        ExcelUtil<ThingsModel> excelUtil = new ExcelUtil<>(ThingsModel.class);
        List<ThingsModel> list = excelUtil.importExcel(file.getInputStream());
        String result = thingsModelService.importData(list, tempSlaveId);
        return AjaxResult.success(result);
    }

    @PreAuthorize("@ss.hasPermi('iot:device:edit')")
    @Log(title = "物模型", businessType = BusinessType.UPDATE)
    @PostMapping("/synchron")
    @ApiOperation("同步采集点模板至产品物模型")
    public AjaxResult synchron(@RequestBody SyncModel model)
    {
        thingsModelService.synchronizeVarTempToProduct(model.getProductIds(), model.getTemplateId());
        return AjaxResult.success();
    }

}
