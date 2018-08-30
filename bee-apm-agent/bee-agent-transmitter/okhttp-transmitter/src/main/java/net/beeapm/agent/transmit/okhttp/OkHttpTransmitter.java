package net.beeapm.agent.transmit.okhttp;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.transmit.AbstractTransmitter;

import java.util.List;

/**
 * Created by yuan on 2018/8/5.
 */
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
