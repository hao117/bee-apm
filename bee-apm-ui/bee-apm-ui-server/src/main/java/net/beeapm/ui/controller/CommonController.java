package net.beeapm.ui.controller;

import net.beeapm.ui.service.ICommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("api/common")
public class CommonController {
    private Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ICommonService commonService;

    @RequestMapping("/getGroupList")
    @ResponseBody
    public Object getGroupList(@RequestBody Map<String,Object> params){
        logger.debug("RequestBody={}",params);
        return commonService.queryGroupList(params);
    }
}
