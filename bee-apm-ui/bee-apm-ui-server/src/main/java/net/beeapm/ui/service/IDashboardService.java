package net.beeapm.ui.service;

import net.beeapm.ui.model.vo.ChartVo;

import java.util.Map;

public interface IDashboardService {
    Map<String,Object> getRequestBarData(Map<String,String> params);
    ChartVo getRequestLineData(Map<String,String> params);
}
