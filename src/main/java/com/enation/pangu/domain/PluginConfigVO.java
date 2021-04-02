package com.enation.pangu.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 执行器vo
 * @author zhangsong
 * @date 2020-11-05
 */
public class PluginConfigVO {
    private List<ExecutorConfigItemVO> itemList;

    public List<ExecutorConfigItemVO> getItemList() {
        return itemList == null ? new ArrayList<>() : itemList;
    }

    public void setItemList(List<ExecutorConfigItemVO> itemList) {
        this.itemList = itemList;
    }

    @Override
    public String toString() {
        return "PluginConfigVO{" +
                "itemList=" + itemList +
                '}';
    }
}
