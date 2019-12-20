package net.beeapm.agent.transmit.elasticsearch;

import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.transmit.AbstractTransmitter;

import java.util.List;

/**
 * @author yuan
 * @date 2018/08/13
 */
@BeePlugin(type = BeePluginType.TRANSMIT, name = "elasticsearch")
public class ElasticSearchTransmitter extends AbstractTransmitter {
    @Override
    public int transmit(Span span) {
        return 0;
    }

    @Override
    public int transmit(List<Span> span) {
        return 0;
    }

    @Override
    public int init() {
        return 0;
    }
}
