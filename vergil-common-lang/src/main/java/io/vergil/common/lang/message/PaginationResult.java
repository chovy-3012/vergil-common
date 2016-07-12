package io.vergil.common.lang.message;

import java.util.List;

/**
 * 带分页的结果封装类
 * 
 * @author zhaowei
 * @date 2015年9月22日下午7:40:39
 * @param <T>
 */
public class PaginationResult<T> {
	private List<T> results;
	private Pagination pagination;

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
}
