package net.beeapm.agent.transmit.elasticsearch;

import net.beeapm.agent.model.Span;
import net.beeapm.agent.transmit.AbstractTransmitter;

import java.util.List;

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
