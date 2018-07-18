package com.personal.asyncsendmail.service;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @author feihu5
 * @date 2018/7/18 17:46
 */
public interface Formatter {

    String format(ILoggingEvent event);
}
