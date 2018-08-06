package net.beeapm.agent.transmit;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.model.Span;

/**
 * Created by yuan on 2018/8/5.
 */
public class ConsoleTransmitter extends AbstractTransmitter {
    public static String name = "console";
    @Override
    public int transmit(Span span) {
        System.out.println(JSON.toJSONString(span));
        return 0;
    }

    @Override
    public int init() {
        return 0;
    }
}
