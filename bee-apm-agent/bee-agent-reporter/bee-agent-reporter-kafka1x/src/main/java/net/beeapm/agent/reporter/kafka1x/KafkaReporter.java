package net.beeapm.agent.reporter.kafka1x;

import com.alibaba.fastjson.JSON;
import net.beeapm.agent.annotation.BeePlugin;
import net.beeapm.agent.annotation.BeePluginType;
import net.beeapm.agent.config.ConfigUtils;
import net.beeapm.agent.log.Log;
import net.beeapm.agent.log.LogFactory;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.reporter.AbstractReporter;
import org.apache.kafka.clients.producer.*;

import java.util.*;

/**
 * @author yuan
 * @date 2018/08/05
 */
@BeePlugin(type = BeePluginType.REPORTER, name = "kafka1x")
public class KafkaReporter extends AbstractReporter {
    private static final Log log = LogFactory.getLog(KafkaReporter.class);
    private Producer kafkaProducer;
    private Properties kafkaConfig;
    private Map<String,String> topicMap;

    @Override
    public int report(Span span) {
        if(span == null){
            return 0;
        }
        try{
            if(kafkaProducer == null){
                initKafkaProducer();
            }
            String topic = topicMap.get(span.getType());
            if(topic == null){
                topic = topicMap.get("default");
            }
            if(topic == null){
                log.debug("类型{}对应的topic为null",span.getType());
                return 0;
            }
            //构造消息体
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, JSON.toJSONString(span));
            //消息发送
            kafkaProducer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        log.error("",exception);
                    }
                }
            });
        }catch (Exception e){
            log.error("",e);
        }
        return 1;
    }

    @Override
    public int report(List<Span> list) {
        int count = 0;
        for(int i = 0; i < list.size(); i++){
            count += report(list.get(i));
        }
        return count;
    }

    @Override
    public int init() {
        topicMap = (Map<String, String>) ConfigUtils.me().getVal("reporter.kafka1x.topic");
        log.error("topicMap = " + topicMap);
        initKafkaProducer();
        return 0;
    }

    private void initKafkaConfig(){
        try {
            Properties properties = new Properties();
            Map<String, Object> configMap = (Map<String, Object>) ConfigUtils.me().getVal("reporter.kafka1x.kafkaConfig");
            Iterator<Map.Entry<String, Object>> iterator = configMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                properties.put(entry.getKey(), entry.getValue().toString());
            }
            if (properties.containsKey(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG)) {
                properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Class.forName((String)properties.get(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG)));
            }else if(!properties.containsKey(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG)) {
                properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Class.forName(KfkConst.DEF_SERIALIZER));
            }
            if (properties.containsKey(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG)) {
                properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Class.forName((String)properties.get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG)));
            }else if (!properties.containsKey(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG)) {
                properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Class.forName(KfkConst.DEF_SERIALIZER));
            }
            if (!properties.containsKey(ProducerConfig.RETRIES_CONFIG)) {
                properties.put(ProducerConfig.RETRIES_CONFIG, KfkConst.DEF_RETRIES);
            }
            if (!properties.containsKey(ProducerConfig.RETRY_BACKOFF_MS_CONFIG)) {
                properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, KfkConst.DEF_RETRY_BACKOFF_MS);
            }
            if (!properties.containsKey(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG)) {
                properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, KfkConst.DEF_REQUEST_TIMEOUT_MS);
            }
            if (properties.containsKey(ProducerConfig.PARTITIONER_CLASS_CONFIG)) {
                properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, Class.forName((String)properties.get(ProducerConfig.PARTITIONER_CLASS_CONFIG)));
            }else if (!properties.containsKey(ProducerConfig.PARTITIONER_CLASS_CONFIG)) {
                properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, Class.forName(KfkConst.DEF_PARTITIONER_CLASS));
            }
            log.error("initKafkaConfig = " + properties);
            kafkaConfig = properties;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initKafkaProducer() {
        if(kafkaConfig == null){
            initKafkaConfig();
        }
        kafkaProducer = new KafkaProducer<String, Object>(kafkaConfig);
    }

    private void stop() {
        if(kafkaProducer!=null){
            kafkaProducer.close();
            kafkaProducer = null;
        }
    }
}
