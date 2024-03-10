package com.webVueBlog.common.utils;

import com.github.pagehelper.PageHelper;
import com.webVueBlog.common.core.page.PageDomain;
import com.webVueBlog.common.core.page.TableSupport;
import com.webVueBlog.common.utils.sql.SqlUtil;

/**
 * 分页工具类
 * 
 *
 */
public class PageUtils extends PageHelper
{
    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }
}
