package net.beeapm.agent.transmit;

import net.beeapm.agent.config.ConfigUtils;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by yuan on 2018/6/28.
 */
public class TransmitterFactory {
    private static final LogImpl log = LogManager.getLog(TransmitterFactory.class.getSimpleName());
    public static AbstractTransmitter transmitter;
    public static Map<String,AbstractTransmitter> transmitterMap;
    private static BlockingQueue<Span> queue ;
    private static ScheduledExecutorService scheduledExecutorService;
    private static String transmitterName;
    private static int idleSleep = ConfigUtils.me().getInt("transmitter.idleSleep",100);
    private static int batchSize = ConfigUtils.me().getInt("transmitter.batchSize",100);
    private static final String THREAD_NAME_PREFIX = "bee-transmitter-";
    public synchronized static int init(){
        System.out.println("===========================>TransmitterFactory::init");
        if(transmitterMap == null) {
            transmitterName = ConfigUtils.me().getStr("transmitter.name");
            transmitterMap = TransmitterLoader.loadTransmitters();
            transmitter = transmitterMap.get(transmitterName);
            if(transmitter == null){
                System.out.println("===========================>transmitter：" + transmitterName + "不存在");
                throw new RuntimeException("transmitter：" + transmitterName + "不存在");
            }
            transmitter.init();
            initQueue();
            initTask();
        }
        return 0;
    }

    private static void initQueue(){
        int queueSize = ConfigUtils.me().getInt("transmitter.queueSize",1000);
        queue = new LinkedBlockingQueue<Span>(queueSize);
    }

    private static void initTask(){
        int threadNum = ConfigUtils.me().getInt("transmitter.threadNum",Runtime.getRuntime().availableProcessors());
        scheduledExecutorService = Executors.newScheduledThreadPool(threadNum);
        for(int i = 0; i < threadNum; i++){
            final int idx = i;
            scheduledExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setName(THREAD_NAME_PREFIX + idx);
                    while (true) {
                        doTransmit();
                    }
                }
            });
        }
    }

    public static void doTransmit(){
        try{
            if(queue.isEmpty()){
                Thread.sleep(idleSleep);
                return;
            }
            List<Span> list = new ArrayList<Span>(batchSize);
            queue.drainTo(list,batchSize);
            transmitter.transmit(list);
        }catch (Exception e){
            log.error("",e);
        }
    }

    public static void transmit(Span span){
        queue.offer(span); //如果队列已满，则返回false
    }
}
