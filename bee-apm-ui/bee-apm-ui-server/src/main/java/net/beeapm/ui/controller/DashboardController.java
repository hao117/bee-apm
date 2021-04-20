package net.beeapm.ui.controller;

import com.alibaba.fastjson.JSON;
import net.beeapm.ui.model.result.ApiResult;
import net.beeapm.ui.model.result.dashboard.NameValue;
import net.beeapm.ui.model.vo.ChartVo;
import net.beeapm.ui.model.vo.ResultVo;
import net.beeapm.ui.service.IDashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("api/dashboard")
public class DashboardController {

    @Autowired
    private IDashboardService dashboardService;
    private Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @RequestMapping("/summary")
    @ResponseBody
    public Object summary(@RequestBody Map<String, Object> reqBody) {
        Object result = dashboardService.summary();
        logger.debug(JSON.toJSONString(result));
        return result;
    }

    @RequestMapping("/stat")
    @ResponseBody
    public Object stat(@RequestBody Map<String, Object> reqBody) {
        Map<String, Object> data = new HashMap<>();
        data.put("log", dashboardService.queryAllCount(reqBody));
        data.put("req", dashboardService.queryRequestCount(reqBody));
        data.put("inst", dashboardService.queryInstCount(reqBody));
        data.put("error", dashboardService.queryErrorCount(reqBody));
        return data;
    }

    @RequestMapping("/getErrorPieData")
    @ResponseBody
    public Object getErrorPieData(@RequestBody Map<String, Object> reqBody) {
        ApiResult<List<NameValue>> result = dashboardService.queryErrorPieData(reqBody);
        return result;
    }

    @RequestMapping("/getErrorLineData")
    @ResponseBody
    public Object getErrorLineData(@RequestBody Map<String, Object> reqBody) {
        return dashboardService.queryErrorLineData(reqBody);
    }

    @RequestMapping("/getRequestBarData")
    @ResponseBody
    public Object getRequestBarData(@RequestBody Map<String, Object> params) {
        logger.debug("RequestBody={}", params);
        return dashboardService.getRequestBarData(params);
    }

    @RequestMapping("/getRequestLineData")
    @ResponseBody
    public Object getRequestLineData(@RequestBody Map<String, Object> params) {
        logger.debug("RequestBody={}", params);
        return dashboardService.getRequestLineData(params);
    }
}
