package net.beeapm.agent.transmit.okhttp;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.transmit.AbstractTransmitter;

import java.util.List;

/**
 * @author yuan
 * @date 2018/08/30
 */
@BeePlugin(type = BeePluginType.TRANSMIT, name = "kafka")
public class OkHttpTransmitter extends AbstractTransmitter {
    public String name = "okhttp";

    @Override
    public int transmit(Span span) {
        OkHttpHelper.getInstance().post(JSON.toJSONString(span));
        return 1;
    }

    @Override
    public int transmit(List<Span> list) {
        OkHttpHelper.getInstance().post(JSON.toJSONString(list));
        return list.size();
    }

    @Override
    public int init() {
        return 0;
    }
}
