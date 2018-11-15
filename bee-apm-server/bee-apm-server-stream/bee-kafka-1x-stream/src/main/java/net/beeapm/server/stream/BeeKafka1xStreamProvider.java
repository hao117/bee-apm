package net.beeapm.server.stream;

import net.beeapm.server.core.common.ConfigHolder;
import net.beeapm.server.core.common.Stream;
import net.beeapm.server.core.stream.AbstractStreamProvider;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BeeKafka1xStreamProvider extends AbstractStreamProvider {
    private final String DEF_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
    private static final Logger logger = LoggerFactory.getLogger(BeeKafka1xStreamProvider.class);
    private AtomicInteger threadNumber = new AtomicInteger(0);
    private Properties kafkaConfig;
    private int IDLE_SLEEP = 500;
    private int POLL_TIMEOUT = 1000;//拉取超时时间，默认500ms

    @Override
    public void start() {
        IDLE_SLEEP = ConfigHolder.getPropInt("bee.provider.kafka1x.idleSleep",500);
        POLL_TIMEOUT = ConfigHolder.getPropInt("bee.provider.kafka1x.pollTimeout",1000);//拉取超时时间，默认1000ms
        logger.info("BeeKafka1xStreamProvider start ............................................");
        initKafkaConfig();
        String[] topics = ConfigHolder.getProperty("bee.provider.kafka1x.topics").split(",");
        int idx = 0;
        for (String topic : topics) {
            Properties config = new Properties();
            config.putAll(kafkaConfig);
            setClientId(config,idx++);
            final KafkaConsumer consumer = createKafkaConsumer(topic, config);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    loopPull(consumer);
                }
            });
            t.setName("bee-kafka-"+threadNumber.getAndIncrement());
            t.start();
        }
    }

    private void initKafkaConfig(){
        if(kafkaConfig != null){
            return;
        }
        Properties props = ConfigHolder.getProperties("bee.provider.kafka1x.kafkaConfig",true);
        if(!props.containsKey(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG)){
            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");
        }
        if(!props.containsKey(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG)){
            props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
        }
        if(!props.containsKey(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG)){
            props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,1048576*5);//5m
        }
        if(!props.containsKey(ConsumerConfig.MAX_POLL_RECORDS_CONFIG)){
            props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,500);//500条
        }
        if(!props.containsKey(ConsumerConfig.GROUP_ID_CONFIG)){
            props.put(ConsumerConfig.GROUP_ID_CONFIG,"bee-consumer-group");
        }
        if(!props.containsKey(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG)){
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,DEF_DESERIALIZER);
        }
        if(!props.containsKey(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG)){
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,DEF_DESERIALIZER);
        }
        kafkaConfig = props;
    }

    private void setClientId(Properties properties,int i){
        String clientId = "bee-";
        String ip = System.getProperty("bee.ip");
        String port = System.getProperty("bee.port");
        if(ip !=null && !ip.isEmpty() && port != null && !port.isEmpty()){
            clientId = clientId + ip + "_" + port;
            clientId = clientId.replace(":","."); //ipv6转换
        }
        logger.debug("clientId = {}",clientId);
        if(clientId != null) {
            properties.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId+i);
        }
    }

    private KafkaConsumer createKafkaConsumer(String topic,Properties props){
        KafkaConsumer consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));
        return consumer;
    }

    private void loopPull(KafkaConsumer consumer){
        while (true){
            try{
                ConsumerRecords<String,String> records = consumer.poll(POLL_TIMEOUT);
                int count = records.count();
                if(count < 1){
                    Thread.sleep(IDLE_SLEEP);
                    continue;
                }
                Iterator<ConsumerRecord<String,String>> it = records.iterator();
                List<String> cacheMsgList = new ArrayList();
                while(it.hasNext()){
                    ConsumerRecord<String,String> record = it.next();
                    String msg = record.value();
                    if(msg != null && !msg.isEmpty()) {
                        //先做缓冲，write写到队列里，队列如果满了，会阻塞住，可能造成kafka自动提交offset超时
                        cacheMsgList.add(msg);
                    }
                }
                for(int i = 0; i < count; i++){
                    write(new Stream(cacheMsgList.get(i)));//QueueStreamHandler里的队列如果满了，会阻塞住
                }
            }catch (Throwable e){
                logger.error("",e);
                if(e instanceof ConnectException){
                    try {
                        Thread.sleep(IDLE_SLEEP);
                    }catch (Exception e2){
                    }
                }
            }
        }
    }





}
