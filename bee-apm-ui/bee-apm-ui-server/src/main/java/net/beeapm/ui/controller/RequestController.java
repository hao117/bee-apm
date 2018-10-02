package net.beeapm.ui.controller;

import net.beeapm.ui.service.IRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("api/request")
public class RequestController {
    private Logger logger = LoggerFactory.getLogger(RequestController.class);

    @Autowired
    private IRequestService requestService;

    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestBody Map<String,Object> params){
        logger.debug("RequestBody={}",params);
        return requestService.list(params);
    }

    @RequestMapping("/chart")
    @ResponseBody
    public Object chart(@RequestBody Map<String,Object> params){
        logger.debug("RequestBody={}",params);
        return requestService.chart(params);
    }
}
