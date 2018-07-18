package com.personal.asyncsendmail.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.personal.asyncsendmail.kafka.Consumer;
import com.personal.asyncsendmail.service.impl.MessageFormatter;
import com.personal.asyncsendmail.service.Formatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author feihu5
 * @date 2018/7/18 17:47
 */
@Slf4j
public class KafkaAppender extends AppenderBase<ILoggingEvent> {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private Formatter formatter;

    @Override
    public void start() {
        if (this.formatter == null) {
            this.formatter = new MessageFormatter();
        }
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    protected void append(ILoggingEvent event) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "1000");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<String, String>(props));
        String logStr = this.formatter.format(event);
        if (logStr != null) {
            kafkaTemplate.send(Consumer.LOG, logStr);
            log.info("send message: topic: " + Consumer.LOG + " message: " + logStr);
        }
    }
}
