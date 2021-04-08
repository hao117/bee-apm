package net.beeapm.ui.service;

import net.beeapm.ui.model.vo.ChartVo;
import net.beeapm.ui.model.vo.ResultVo;
import java.util.Map;

public interface IDashboardService {
    ResultVo getRequestBarData(Map<String,Object> params);
    ChartVo getRequestLineData(Map<String,Object> params);
    Long queryInstCount(Map<String,Object> params);
    Long queryAllCount(Map<String,Object> params);
    Long queryRequestCount(Map<String,Object> params);
    Long queryErrorCount(Map<String,Object> params);
    ResultVo queryErrorPieData(Map<String,Object> params);
    ResultVo queryErrorLineData(Map<String,Object> params);
}
