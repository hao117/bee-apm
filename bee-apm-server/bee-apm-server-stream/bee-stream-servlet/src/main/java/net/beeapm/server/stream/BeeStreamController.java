package net.beeapm.server.stream;

import net.beeapm.server.core.common.Stream;
import net.beeapm.server.core.handler.HandlerFactory;
import net.beeapm.server.core.stream.AbstractStreamProvider;
import net.beeapm.server.core.stream.IStreamProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author yuan
 * @date 2018/08/27
 */
@Controller
@RequestMapping
public class BeeStreamController {
    private static final Logger logger = LoggerFactory.getLogger(BeeStreamController.class);

    @RequestMapping("/stream")
    @ResponseBody
    public String stream(@RequestBody String body){
        try {
            HandlerFactory.getInstance().executeFirstHandler(new Stream(body));
            return "ok";
        }catch (Exception e){
            logger.error("",e);
        }
        return "fail";
    }
}
