package net.beeapm.agent.transmit;


import net.beeapm.agent.model.Span;

/**
 * Created by yuan on 2018/8/5.
 */
public abstract class AbstractTransmitter {
    private String name;
    public abstract int transmit(Span span);
    public abstract int init();
}
