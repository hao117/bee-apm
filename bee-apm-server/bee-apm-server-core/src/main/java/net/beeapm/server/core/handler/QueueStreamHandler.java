package net.beeapm.server.core.handler;

import net.beeapm.server.core.common.ConfigHolder;
import net.beeapm.server.core.common.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

public class QueueStreamHandler extends AbstractStreamHandler {
    private static final Logger logger = LoggerFactory.getLogger(QueueStreamHandler.class);
    private static BlockingQueue<Object> queue ;
    private static ScheduledExecutorService scheduledExecutorService;
    //下一步处理操作线程数
    private static int threadNum;
    //队列空时候休眠多少时间再进行读取数据处理
    private static int sleep;
    //队列长度大小
    private static int queueSize;
    //最大批量值，每次从队列里最多提取的数据数量
    private static int batchSize;
    //累积循环次数限制，数据达不到batchSize值不发送的累积次数限制
    private static int maxLoopTimes;
    private final static String THREAD_NAME_PREFIX = "hlog-stream-thread-";

    private void initConfig(){
        threadNum = Integer.parseInt(ConfigHolder.getProperty("bee.handlers.queue.threadNum",Runtime.getRuntime().availableProcessors()+""));
        queueSize = Integer.parseInt(ConfigHolder.getProperty("bee.handlers.queue.queueSize","2000"));
        sleep = Integer.parseInt(ConfigHolder.getProperty("bee.handlers.queue.sleep","100"));
        batchSize = Integer.parseInt(ConfigHolder.getProperty("bee.handlers.queue.batchSize","100"));
        maxLoopTimes = Integer.parseInt(ConfigHolder.getProperty("bee.handlers.queue.maxLoopTimes","3"));
    }

    @Override
    public void doInit() throws Exception {
        logger.debug("init .......");
        initConfig();
        queue = new LinkedBlockingQueue<>(queueSize);
        scheduledExecutorService = Executors.newScheduledThreadPool(threadNum);
        doNextHandlerTask();
    }

    @Override
    public void handle(Stream stream) throws Exception {
        Object src = stream.getSource();
        if(src.getClass().isArray()){
            Object[] array = (Object[])src;
            for(Object object : array){
                queue.put(object);
            }
        }else{
            queue.put(src);
        }
        stream.setSource(null);// 告诉下游，数据没了
    }


    private void doNextHandler(){
        try {
            List<Object> buffList = new ArrayList<Object>(batchSize);
            //循环获取队列的数据，如果数据量小于设置的批次量，休眠一定时间再次获取，循环获取的次数小于设置的limitTimes阀值
            int times = 0;
            do{
                if(times > 0){
                    Thread.sleep(sleep);
                }
                times++;
                queue.drainTo(buffList, batchSize - buffList.size());
                if(maxLoopTimes < times && !buffList.isEmpty()){
                    break;
                }
            }while (buffList.size() < batchSize);
            logger.debug("缓冲批量处理loopTimes={},buffList.size={}.........", times, buffList.size());
            if (!buffList.isEmpty()) {
                Stream stream = new Stream(buffList.toArray());
                HandlerFactory.getInstance().executeHandler(stream,this.getNextStreamHandler());

            }
        }catch (Exception e){
            logger.error("缓冲批量处理DataItemQueue异常", e);
        }
    }

    private void doNextHandlerTask(){
        logger.debug("doTask DataItemQueue .......");
        for(int i = 0; i < threadNum; i++){
            final int idx = i;
            scheduledExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setName(THREAD_NAME_PREFIX + idx);
                    while (true) {
                        try {
                            doNextHandler();
                        } catch (Exception e) {
                            logger.error("", e);
                        }
                    }
                }
            });
        }
    }


}
