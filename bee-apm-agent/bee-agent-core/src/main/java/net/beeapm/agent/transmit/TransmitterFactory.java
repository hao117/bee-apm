package net.beeapm.agent.transmit;

import net.beeapm.agent.model.Span;

import java.util.Map;

/**
 * Created by yuan on 2018/6/28.
 */
public class TransmitterFactory {
    public static AbstractTransmitter transmitter;
    public static Map<String,AbstractTransmitter> transmitterMap;
    public synchronized static int init(){
        System.out.println("===========================>TransmitterFactory::init");
        if(transmitterMap == null) {
            transmitterMap = TransmitterLoader.loadTransmitters();
            transmitter = transmitterMap.get("console");
            transmitter.init();
        }
        return 0;
    }

    public static void transmit(Span span){
        if(transmitter == null){
            init();
        }
        transmitter.transmit(span);
    }
}
