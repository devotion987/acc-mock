package org.apache.rocketmq.console.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * guangyao.wu 2018/8/16 13:13
 */
@Component
public class AccMqConsumer {

    private static final Logger msgLog = LoggerFactory.getLogger("msgLog");
    private static final Logger logger = LoggerFactory.getLogger(AccMqConsumer.class);

    @Value("${acc.consume.nameServer}")
    private String nameServer;
    @Value("${acc.consume.groupName}")
    private String groupName;

    @Value("${acc.consume.topic}")
    private String subscribeTopic;

    @Value("${acc.consume.open}")
    private boolean open;

    @PostConstruct
    public void pushConsumer() {
        if (!open || StringUtils.isEmpty(subscribeTopic))
            return;
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(nameServer);

        try {
            String[] topics = subscribeTopic.split(",");
            for (String topic : topics)
                consumer.subscribe(topic, "*");
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

            consumer.registerMessageListener(new MessageListenerConcurrently() {

                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    StringBuilder sb = new StringBuilder();
                    for (MessageExt msg : msgs) {
                        sb.append(new String(msg.getBody()));
                    }
                    msgLog.info("收到消息：{}", sb.toString());
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            consumer.start();

            logger.info("消息消费方启动成功...");
        } catch (Exception e) {
            logger.error("消息消费方启动失败：", e);
        }
    }

}
