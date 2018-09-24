package net.beeapm.ui.controller;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("api/dashboard")
public class DashboardController {
    @RequestMapping("/stat")
    @ResponseBody
    public Map stat(){
        Map<String,String> data = new HashMap<>();
        data.put("log","2345");
        data.put("req","1234");
        data.put("inst","23");
        data.put("error","56");
        System.out.println("=============dashboard>>>" + DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        return data;
    }
}
