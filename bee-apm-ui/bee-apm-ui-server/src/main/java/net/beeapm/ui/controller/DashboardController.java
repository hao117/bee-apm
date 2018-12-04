package net.beeapm.ui.controller;

import com.alibaba.fastjson.JSON;
import net.beeapm.ui.model.KeyValue;
import net.beeapm.ui.model.vo.ChartVo;
import net.beeapm.ui.model.vo.ResultVo;
import net.beeapm.ui.service.IDashboardService;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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

    @RequestMapping("/stat")
    @ResponseBody
    public Map stat(@RequestBody Map<String,Object> reqBody){
        Map<String,Object> data = new HashMap<>();
        data.put("log", dashboardService.queryAllCount(reqBody));
        data.put("req",dashboardService.queryRequestCount(reqBody));
        data.put("inst",dashboardService.queryInstCount(reqBody));
        data.put("error",dashboardService.queryErrorCount(reqBody));
        return data;
    }

    @RequestMapping("/getErrorPieData")
    @ResponseBody
    public ResultVo getErrorPieData(@RequestBody Map<String,Object> reqBody){
        ResultVo result = dashboardService.queryErrorPieData(reqBody);
        return result;
    }

    @RequestMapping("/getErrorLineData")
    @ResponseBody
    public Map getErrorLineData(@RequestBody Map<String,Object> reqBody){
        ResultVo resultVo = dashboardService.queryErrorLineData(reqBody);
        Map res = (Map)resultVo.getResult();
        res.put("code",0);
        logger.debug("ErrorLineData = {}",JSON.toJSONString(res));
        return res;
    }

    @RequestMapping("/getRequestBarData")
    @ResponseBody
    public Map<String,Object> getRequestBarData(@RequestBody Map<String,String> params){
        logger.debug("RequestBody={}",params);
        return dashboardService.getRequestBarData(params);
    }
    @RequestMapping("/getRequestLineData")
    @ResponseBody
    public ChartVo getRequestLineData(@RequestBody Map<String, String> params){
        logger.debug("RequestBody={}",params);
        return dashboardService.getRequestLineData(params);
    }
}
