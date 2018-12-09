package net.beeapm.ui.controller;

import net.beeapm.ui.service.ILoggerService;
import net.beeapm.ui.service.IMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("api/logger")
public class LoggerController {
    private Logger logger = LoggerFactory.getLogger(LoggerController.class);

    @Autowired
    private ILoggerService loggerService;

    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestBody Map<String,Object> params){
        logger.debug("RequestBody={}",params);
        return loggerService.list(params);
    }

    @RequestMapping("/chart")
    @ResponseBody
    public Object chart(@RequestBody Map<String,Object> params){
        logger.debug("RequestBody={}",params);
        return loggerService.chart(params);
    }
}
