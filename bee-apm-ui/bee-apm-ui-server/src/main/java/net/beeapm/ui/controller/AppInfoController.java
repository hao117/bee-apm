package net.beeapm.ui.controller;

import net.beeapm.ui.service.IAppInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
/**
 * @author yuan
 * @date 2020-01-31
 */
@Controller
@RequestMapping("api/app/info")
public class AppInfoController {
    private Logger logger = LoggerFactory.getLogger(AppInfoController.class);

    @Autowired
    private IAppInfoService appInfoService;

    @RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestBody Map<String,Object> params){
        logger.debug("RequestBody={}",params);
        return appInfoService.list(params);
    }
}
