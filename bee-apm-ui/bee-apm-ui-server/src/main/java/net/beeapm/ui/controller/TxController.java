package net.beeapm.ui.controller;

import net.beeapm.ui.service.ISqlService;
import net.beeapm.ui.service.ITxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("api/tx")
public class TxController {
    private Logger logger = LoggerFactory.getLogger(TxController.class);

    @Autowired
    private ITxService service;

    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestBody Map<String,Object> params){
        logger.debug("RequestBody={}",params);
        return service.list(params);
    }

    @RequestMapping("/chart")
    @ResponseBody
    public Object chart(@RequestBody Map<String,Object> params){
        logger.debug("RequestBody={}",params);
        return service.chart(params);
    }
}
