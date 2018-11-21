package net.beeapm.ui.service;

import net.beeapm.ui.model.vo.ChartVo;
import net.beeapm.ui.model.vo.ResultVo;
import java.util.Map;

public interface IDashboardService {
    Map<String,Object> getRequestBarData(Map<String,String> params);
    ChartVo getRequestLineData(Map<String,String> params);
    Long queryInstCount(Map<String,Object> params);
    Long queryAllCount(Map<String,Object> params);
    Long queryRequestCount(Map<String,Object> params);
    Long queryErrorCount(Map<String,Object> params);
    ResultVo queryErrorPieData(Map<String,Object> params);
}
