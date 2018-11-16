package net.beeapm.agent.transmit.rocketmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import net.beeapm.agent.config.ConfigUtils;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.transmit.AbstractTransmitter;

import java.util.*;

/**
 * Created by kaddddd on 2018/8/5.
 */
public class RocketMqTransmitter extends AbstractTransmitter {
    private static final LogImpl log = LogManager.getLog(RocketMqTransmitter.class);
    private DefaultMQProducer rocketMqProducer;
    private String nameSrvAddr;
    private String instanceName;
    private String producerGroup;

    private Map<String,String> topicMap;

    @Override
    public int transmit(Span span) {
        if(span == null){
            return 0;
        }
        try{
            String topic = topicMap.get(span.getType());
            if(topic == null){
                topic = topicMap.get("default");
            }
            if(topic == null){
                log.debug("类型{}对应的topic为null",span.getType());
                return 0;
            }
            byte[] bytes = JSON.toJSONString(span).getBytes(RemotingHelper.DEFAULT_CHARSET);
            final Message msg = new Message(topic,// topic
                    RmqConst.tags,// tag
                    RmqConst.key,// key
                    bytes// body
            );
            rocketMqProducer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    //System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                    System.out.println("发送成功---"+ new String (msg.getBody()));
                }

                @Override
                public void onException(Throwable e) {
                    System.out.printf("%-10d Exception %s %n", e);
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            log.error("",e);
        }
        return 1;
    }

    @Override
    public int transmit(List<Span> list) {
        int count = 0;
        for(int i = 0; i < list.size(); i++){
            count += transmit(list.get(i));
        }
        return count;
    }

    @Override
    public int init() {
        topicMap = (Map<String, String>) ConfigUtils.me().getVal("transmitter.rocketmq.topic");
        log.error("topicMap = " + topicMap);
        try{
            initRocketMqProducer();
        }catch (MQClientException e){
            log.error(e.getErrorMessage());
        }

        return 0;
    }

    private void initRocketMqConfig(){
        try {
            nameSrvAddr = (String) ConfigUtils.me().getVal("transmitter.rocketmq.nameSrvAddr");
            instanceName = (String) ConfigUtils.me().getVal("transmitter.rocketmq.instanceName");
            producerGroup = (String) ConfigUtils.me().getVal("transmitter.rocketmq.producerGroup");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initRocketMqProducer() throws MQClientException {
        initRocketMqConfig();
        rocketMqProducer = new DefaultMQProducer(producerGroup);
        rocketMqProducer.setNamesrvAddr(nameSrvAddr);
        rocketMqProducer.setInstanceName(instanceName);
        rocketMqProducer.setVipChannelEnabled(false);// // 必须设为false否则连接broker10909端口
        rocketMqProducer.start();
    }

    private void stop() {
        if(rocketMqProducer!=null){
            rocketMqProducer = null;
        }
    }
}
