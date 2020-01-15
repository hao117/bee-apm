package net.beeapm.demo.controller;

import com.alibaba.fastjson.JSON;
import net.beeapm.demo.model.RequestVo;
import net.beeapm.demo.model.ResultVo;
import net.beeapm.demo.service.IDuckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuanlong.chen
 * @date 2020/01/12
 */
@RequestMapping("/duck")
@RestController
public class DuckController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IDuckService service;

    @RequestMapping("/sayGaGa")
    public ResultVo sayGaGa(@RequestBody RequestVo req){
        logger.debug(JSON.toJSONString(req));
        return service.sayGaGa(req);
    }

    @RequestMapping("/twoWing")
    public ResultVo twoWing(@RequestBody RequestVo req){
        logger.debug(JSON.toJSONString(req));
        return service.twoWing(req);
    }

    @RequestMapping("/twoFoot")
    public ResultVo twoFoot(@RequestBody RequestVo req){
        logger.debug(JSON.toJSONString(req));
        return service.twoFoot(req);
    }

    @RequestMapping("/whiteDuck")
    public ResultVo whiteDuck(@RequestBody RequestVo req){
        logger.debug(JSON.toJSONString(req));
        return service.whiteDuck(req);
    }
}
