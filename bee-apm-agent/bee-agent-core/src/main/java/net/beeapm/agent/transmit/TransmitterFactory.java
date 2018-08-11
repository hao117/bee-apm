package net.beeapm.agent.transmit;

import net.beeapm.agent.model.Span;

/**
 * Created by yuan on 2018/6/28.
 */
public class TransmitterFactory {
    public static AbstractTransmitter transmitter;
    public static int init(){
        transmitter = new ConsoleTransmitter();
        return 0;
    }

    public static void transmit(Span span){
        if(transmitter == null){
            init();
        }
        transmitter.transmit(span);
    }
}
