package net.beeapm.ui.controller;

import com.alibaba.fastjson.JSON;
import net.beeapm.ui.model.KeyValue;
import net.beeapm.ui.model.vo.ChartVo;
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
        dashboardService.queryInstCount(reqBody);
        data.put("log", RandomUtils.nextInt(1001,5000));
        data.put("req",RandomUtils.nextInt(300,500));
        data.put("inst",dashboardService.queryInstCount(reqBody));
        data.put("error",RandomUtils.nextInt(200,500));
        System.out.println("=============dashboard>>>" + DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        return data;
    }

    @RequestMapping("/getErrorPieData")
    @ResponseBody
    public Map getErrorPieData(@RequestBody Map<String,Object> reqBody){
        System.out.println("getErrorPieData RequestBody : "+JSON.toJSONString(reqBody));
        List list = new ArrayList();
        list.add(new KeyValue("crm3-app1",24234));
        list.add(new KeyValue("crm3-app2",4546));
        list.add(new KeyValue("crm3-app3",4527));
        list.add(new KeyValue("crm3-app4",343));
        list.add(new KeyValue("crm3-app5",7345));
        list.add(new KeyValue("crm3-app6",2323));
        list.add(new KeyValue("crm3-app7",345));
        list.add(new KeyValue("crm3-app8",2345));
        list.add(new KeyValue("crm3-app9",1300));
        list.add(new KeyValue("crm3-app10",1246));
        list.add(new KeyValue("crm3-app11",3248));
        list.add(new KeyValue("crm3-app12",521));
        Map res = new HashMap();
        res.put("list",list);
        res.put("code",0);
        System.out.println("getErrorPieData Result : "+JSON.toJSONString(res));
        return res;
    }

    @RequestMapping("/getErrorLineData")
    @ResponseBody
    public Map getErrorLineData(@RequestBody Map<String,Object> reqBody){
        System.out.println("getErrorLineData RequestBody : "+JSON.toJSONString(reqBody));
        List list = new ArrayList();
        Map<String,Object> item = new HashMap<>();
        item.put("time","22:15:01");
        item.put("app1",98);
        item.put("app2",322);
        item.put("app3",246);
        item.put("app4",211);
        item.put("app5",123);
        list.add(item);
        item = new HashMap<>();
        item.put("time","22:15:02");
        item.put("app1",334);
        item.put("app2",221);
        item.put("app3",781);
        item.put("app4",123);
        item.put("app5",456);
        list.add(item);
        item = new HashMap<>();
        item.put("time","22:15:03");
        item.put("app1",128);
        item.put("app2",246);
        item.put("app3",258);
        item.put("app4",124);
        item.put("app5",842);
        list.add(item);
        item = new HashMap<>();
        item.put("time","22:15:04");
        item.put("app1",253);
        item.put("app2",732);
        item.put("app3",252);
        item.put("app4",532);
        item.put("app5",231);
        list.add(item);
        item = new HashMap<>();
        item.put("time","22:15:05");
        item.put("app1",134);
        item.put("app2",742);
        item.put("app3",231);
        item.put("app4",573);
        item.put("app5",221);
        list.add(item);
        item = new HashMap<>();
        item.put("time","22:15:06");
        item.put("app1",234);
        item.put("app2",132);
        item.put("app3",210);
        item.put("app4",456);
        item.put("app5",211);
        list.add(item);
        item = new HashMap<>();
        item.put("time","22:15:07");
        item.put("app1",174);
        item.put("app2",827);
        item.put("app3",433);
        item.put("app4",356);
        item.put("app5",258);
        list.add(item);
        item = new HashMap<>();
        item.put("time","22:15:08");
        item.put("app1",135);
        item.put("app2",874);
        item.put("app3",342);
        item.put("app4",234);
        item.put("app5",743);
        list.add(item);
        item = new HashMap<>();
        item.put("time","22:15:09");
        item.put("app1",652);
        item.put("app2",422);
        item.put("app3",311);
        item.put("app4",677);
        item.put("app5",333);
        list.add(item);

        Map res = new HashMap();
        res.put("columns",new String[]{"time","app1","app2","app3","app4","app5"});
        res.put("rows",list);
        res.put("code",0);
        System.out.println("getErrorLineData Result : "+JSON.toJSONString(res));
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
