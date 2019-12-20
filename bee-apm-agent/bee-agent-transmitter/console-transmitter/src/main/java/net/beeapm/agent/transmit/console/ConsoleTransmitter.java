package net.beeapm.agent.transmit.console;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.transmit.AbstractTransmitter;

import java.util.List;


/**
 * @author yuan
 * @date 2018/08/05
 */
@BeePlugin(type = BeePluginType.TRANSMIT, name = "console")
public class ConsoleTransmitter extends AbstractTransmitter {
    public String name = "console";

    @Override
    public int transmit(Span span) {
        System.out.println(JSON.toJSONString(span));
        return 0;
    }

    @Override
    public int transmit(List<Span> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            transmit(list.get(i));
        }
        return size;
    }

    @Override
    public int init() {
        return 0;
    }
}
