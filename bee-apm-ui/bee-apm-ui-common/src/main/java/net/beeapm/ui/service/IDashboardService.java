package net.beeapm.ui.service;

import net.beeapm.ui.model.result.ApiResult;
import net.beeapm.ui.model.result.dashboard.SummaryResult;
import net.beeapm.ui.model.vo.ChartVo;
import net.beeapm.ui.model.vo.ResultVo;

import java.util.List;
import java.util.Map;

public interface IDashboardService {
    ResultVo getRequestBarData(Map<String, Object> params);

    ChartVo getRequestLineData(Map<String, Object> params);

    Long queryInstCount(Map<String, Object> params);

    Long queryAllCount(Map<String, Object> params);

    Long queryRequestCount(Map<String, Object> params);

    Long queryErrorCount(Map<String, Object> params);

    ResultVo queryErrorPieData(Map<String, Object> params);

    ResultVo queryErrorLineData(Map<String, Object> params);

    /**
     * 当天汇总统计
     *
     * @return
     */
    ApiResult<List<SummaryResult>> summary();

}
