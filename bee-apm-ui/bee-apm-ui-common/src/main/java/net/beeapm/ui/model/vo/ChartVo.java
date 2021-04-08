package net.beeapm.ui.model.vo;

import java.util.List;

public class ChartVo extends BaseVo{
    private String[] columns;
    private List<Object> rows;

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public List<Object> getRows() {
        return rows;
    }

    public void setRows(List<Object> rows) {
        this.rows = rows;
    }
}
