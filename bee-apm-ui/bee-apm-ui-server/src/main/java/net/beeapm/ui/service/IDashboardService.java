package net.beeapm.ui.service;

import java.util.Map;

public interface IDashboardService {
    Map<String,Object> getRequestBarData(Map<String,String> params);
    Map<String,Object> getRequestLineData(Map<String,String> params);
}
