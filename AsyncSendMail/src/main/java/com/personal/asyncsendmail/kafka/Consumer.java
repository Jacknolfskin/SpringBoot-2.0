
package com.personal.asyncsendmail.kafka;

import com.personal.asyncsendmail.entity.Log;
import com.personal.asyncsendmail.service.LogService;
import com.personal.asyncsendmail.util.UCUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Consumer
 */
@Slf4j
@Component
public class Consumer {

    public static final String LOGIN_STAT = "login_stat";

    public static final String LOG = "center_log";

    @Resource
    private LogService logService;

    public static Map<String, ThreadHolder> map = new HashMap<>();

    public void process(ConsumerRecord record) {
        long startTime = System.currentTimeMillis();
        String topic = record.topic();
        String key = "";
        if (record.key() != null) {
            key = record.key().toString();
        }
        String message = record.value().toString();
        if (LOG.equals(topic)) {
            String[] split = message.split(UCUtil.SPLIT);
            String level = split[0];
            String type = split[2];
            Long commonId = Long.valueOf(split[3]);
            String content = split[4];
            // 异步存储日志
            Log oldLog = logService.findOldLog(commonId, type, content);
            if (oldLog != null) {
                oldLog.setCount(oldLog.getCount() + 1);
                logService.save(oldLog);
            } else {
                Log log = new Log(level, content, type, commonId, 1);
                logService.save(log);
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("SubmitConsumer.time=" + (endTime - startTime));
    }
}
