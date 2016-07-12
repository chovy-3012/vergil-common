package io.vergil.common.lang.message;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @Description 分页-begin从1开始，适用oracle
 * @author zhaowei
 * @date 2015年6月28日下午4:58:28
 */
public class Pagination {

	// 记录开始位置
	private int begin;
	// 记录结束位置
	private int end;
	// 当前页码
	private int currentPage;
	// 每页显示记录数量
	private int pageSize;
	// 总共的结果的数量
	private long totalRecords;
	// 总共的显示页数
	private int totalPage;

	/**
	 * @param currentPage
	 *            当前页码
	 * @param pageSize
	 *            每页显示数量
	 */
	public Pagination(int currentPage, int pageSize) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}

	public int getBegin() {
		return begin > 0 || end > 0 ? begin : (this.currentPage - 1) * this.pageSize + 1;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return begin > 0 || end > 0 ? end : this.currentPage * this.pageSize;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getTotalPage() {
		if (getTotalRecords() <= 0 || pageSize <= 0) {
			totalPage = 0;
		} else if (totalRecords % pageSize == 0) {
			totalPage = (int) (totalRecords / pageSize);
		} else {
			totalPage = (int) (totalRecords / pageSize + 1);
		}
		return totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this, SerializerFeature.WriteMapNullValue);
	}
}
