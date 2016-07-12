package io.vergil.common.lang.message;

import java.util.List;

/**
 * 带分页的结果封装类
 * 
 * @author zhaowei
 * @date 2015年9月22日下午7:40:39
 * @param <T>
 */
public class PaginationResult2<T> {
	private List<T> results;
	private Pagination2 pagination2;

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

    public Pagination2 getPagination2() {
        return pagination2;
    }

    public void setPagination2(Pagination2 pagination2) {
        this.pagination2 = pagination2;
    }

}
