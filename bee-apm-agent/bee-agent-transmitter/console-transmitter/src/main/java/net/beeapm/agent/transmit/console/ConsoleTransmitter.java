package net.beeapm.agent.transmit.console;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.transmit.AbstractTransmitter;

import java.util.List;

/**
 * Created by yuan on 2018/8/5.
 */
public class ConsoleTransmitter extends AbstractTransmitter {
    public String name = "console";

    @Override
    public int transmit(Span span) {
        System.out.println(JSON.toJSONString(span));
        return 0;
    }

    @Override
    public int transmit(List<Span> list) {
        if(list == null || list.isEmpty()){
            return 0;
        }
        int size = list.size();
        for(int i = 0; i < size; i++){
            transmit(list.get(i));
        }
        return size;
    }

    @Override
    public int init() {
        return 0;
    }
}
