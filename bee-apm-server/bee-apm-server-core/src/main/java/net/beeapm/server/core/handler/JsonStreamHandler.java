package net.beeapm.server.core.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.server.core.common.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author yuan
 * @date 2018/08/27
 */
@BeePlugin(type = BeePluginType.HANDLER, name = "json")
public class JsonStreamHandler extends AbstractStreamHandler {
    private static final Logger logger = LoggerFactory.getLogger(JsonStreamHandler.class);
    @Override
    public void doInit() throws Exception {
        logger.debug("init .......");
    }

    @Override
    public void handle(Stream stream) throws Exception {
        String json = (String) stream.getSource();
        if(json.startsWith("[")){
            JSONArray array = JSON.parseArray(json);
            stream.setSource(array.toArray());

        }else{
            stream.setSource(JSON.parseObject(json));
        }

    }
}
