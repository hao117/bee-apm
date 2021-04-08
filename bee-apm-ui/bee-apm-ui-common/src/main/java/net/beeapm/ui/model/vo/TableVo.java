package net.beeapm.ui.model.vo;

import java.util.List;

public class TableVo extends BaseVo{
    private List<Object> rows;
    private Integer pageNum;
    private Long pageTotal;

    public List<Object> getRows() {
        return rows;
    }

    public void setRows(List<Object> rows) {
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
