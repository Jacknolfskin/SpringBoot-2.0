package com.personal.asyncsendmail.service.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.personal.asyncsendmail.service.Formatter;
import com.personal.asyncsendmail.util.UCUtil;

/**
 * @author feihu5
 * @date 2018/7/18 17:46
 */
public class MessageFormatter implements Formatter {
    @Override
    public String format(ILoggingEvent event) {
        if (event.getFormattedMessage().startsWith(UCUtil.USER_LOG)) {
            return event.getLevel().toString() + UCUtil.SPLIT + event.getFormattedMessage();
        } else {
            return null;
        }
    }
}
