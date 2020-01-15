package net.beeapm.demo.controller;

import com.alibaba.fastjson.JSON;
import net.beeapm.demo.model.RequestVo;
import net.beeapm.demo.model.ResultVo;
import net.beeapm.demo.service.IDogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuanlong.chen
 * @date 2020/01/12
 */
@RequestMapping("/dog")
@RestController
public class DogController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IDogService service;

    @RequestMapping("/blackDog")
    public ResultVo blackDog(@RequestBody RequestVo req){
        logger.debug(JSON.toJSONString(req));
        return service.blackDog(req);
    }

    @RequestMapping("/sayWangWang")
    public ResultVo sayWangWang(@RequestBody RequestVo req){
        logger.debug(JSON.toJSONString(req));
        return service.sayWangWang(req);
    }

    @RequestMapping("/sayHelloDog")
    public ResultVo sayHelloDog(@RequestBody RequestVo req){
        logger.debug(JSON.toJSONString(req));
        return service.sayHelloDog(req);
    }

    @RequestMapping("/sayGoodbyeDog")
    public ResultVo sayGoodbyeDog(@RequestBody RequestVo req){
        logger.debug(JSON.toJSONString(req));
        return service.sayGoodbyeDog(req);
    }
}
