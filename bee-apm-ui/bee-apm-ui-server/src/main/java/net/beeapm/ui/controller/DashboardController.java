package net.beeapm.ui.controller;

import net.beeapm.ui.model.result.ApiResult;
import net.beeapm.ui.model.result.dashboard.NameValue;
import net.beeapm.ui.service.IDashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @date 2018/9/24
 * @author yuan
 */
@Controller
@RequestMapping("api/dashboard")
public class DashboardController {

    @Autowired
    private IDashboardService dashboardService;
    private Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @RequestMapping("/summary")
    @ResponseBody
    public Object summary() {
        Object result = dashboardService.summary();
        return result;
    }

    @RequestMapping("/getErrorPieData")
    @ResponseBody
    public Object getErrorPieData() {
        ApiResult<List<NameValue>> result = dashboardService.queryErrorPieData();
        return result;
    }

    @RequestMapping("/getErrorLineData")
    @ResponseBody
    public Object getErrorLineData() {
        return dashboardService.queryErrorLineData();
    }

    @RequestMapping("/getRequestBarData")
    @ResponseBody
    public Object getRequestBarData() {
        return dashboardService.getRequestBarData();
    }

    @RequestMapping("/getRequestLineData")
    @ResponseBody
    public Object getRequestLineData() {
        return dashboardService.getRequestLineData();
    }
}
