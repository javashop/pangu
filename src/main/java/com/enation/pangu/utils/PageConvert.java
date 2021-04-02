package com.enation.pangu.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.enation.pangu.model.WebPage;

/**
 * 分页数据转换工具类
 * @author duanmingyu
 * @version v1.0
 * @since v7.2.2
 * 2020-07-20
 */
public class PageConvert {

    /**
     * 分页数据转换
     * 将mybatis的分页数据转为自定义分页数据
     * @param iPage
     * @return
     */
    public static WebPage convert(IPage iPage) {
        WebPage page = new WebPage();
        page.setData(iPage.getRecords());

        page.setRecordsTotal(iPage.getTotal());
        return page;
    }

}
