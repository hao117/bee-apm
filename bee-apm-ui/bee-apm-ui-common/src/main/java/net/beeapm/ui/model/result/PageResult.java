package net.beeapm.ui.model.result;

import java.util.List;

/**
 * 分页查询返回值
 *
 * @author yuan
 */
public class PageResult<T> extends ApiResult {
    private List<T> rows;
    private Integer pageNum;
    private Long pageTotal;

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Long getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Long pageTotal) {
        this.pageTotal = pageTotal;
    }

}
