package com.enation.pangu.model;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象. 包含当前页数据及分页信息如总记录数.
 *
 * @param <T> 数据类型
 * @author kingapex
 * @version v1.0
 * @since v7.0.0
 * 2017年8月15日 上午10:55:08
 */
public class WebPage<T> implements Serializable {

	/**
	 * 数据列表
	 */
	private List<T> data;



	/**
	 * 总计录数
	 */
	private Long recordsTotal;

	/**
	 *	datatables 所需要的参数
	 */
	private long draw;

	public long getRecordsFiltered() {
		return  this.recordsTotal;
	}


	public long getDraw() {
		return draw;
	}

	public void setDraw(long draw) {
		this.draw = draw;
	}

	/**
	 * 构造方法
	 *
	 * @param data      数据列表
	 * @param pageNo    当前页码
	 * @param pageSize  页大小
	 * @param recordsTotal 总计录数
	 */
	public WebPage(Long pageNo, Long recordsTotal, Long pageSize, List<T> data) {
		this.data = data;
		this.recordsTotal = recordsTotal;
	}

	public WebPage() {
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}


	public Long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(Long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	@Override
	public String toString() {
		return "WebPage{" +
				"data=" + data +
				", dataTotal=" + recordsTotal +
				'}';
	}

}
