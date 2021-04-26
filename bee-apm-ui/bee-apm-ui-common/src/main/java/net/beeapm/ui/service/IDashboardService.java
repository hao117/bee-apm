package net.beeapm.ui.service;

import net.beeapm.ui.model.result.ApiResult;
import net.beeapm.ui.model.result.dashboard.NameValue;
import net.beeapm.ui.model.result.dashboard.SummaryResult;

import java.util.List;
import java.util.Map;

public interface IDashboardService {
    /**
     * 请求耗时区间统计
     * @return
     */
    ApiResult<List<NameValue>> getRequestBarData();

    /**
     * 请求量趋势
     * @return
     */
    ApiResult<Map<String, List<Integer>>> getRequestLineData();

    /**
     * 异常占比
     * @return
     */
    ApiResult<List<NameValue>> queryErrorPieData();

    /**
     * 异常趋势
     * @return
     */
    ApiResult<Map<String, List<Integer>>> queryErrorLineData();

    /**
     * 当天汇总统计
     *
     * @return
     */
    ApiResult<List<SummaryResult>> summary();

}
