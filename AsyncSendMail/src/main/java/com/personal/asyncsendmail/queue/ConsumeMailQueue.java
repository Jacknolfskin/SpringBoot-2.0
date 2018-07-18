package com.personal.asyncsendmail.queue;

import com.personal.asyncsendmail.entity.Email;
import com.personal.asyncsendmail.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * feihu5
 */
@Component
public class ConsumeMailQueue {
    private static final Logger logger = LoggerFactory.getLogger(ConsumeMailQueue.class);

    @Autowired
    MailService mailService;

    @PostConstruct
    public void startThread() {
        // 两个大小的固定线程池
        ExecutorService e = Executors.newFixedThreadPool(2);
        e.submit(new PollMail(mailService));
        e.submit(new PollMail(mailService));
    }

    @PreDestroy
    public void stopThread() {
        logger.info("destroy");
    }

    class PollMail implements Runnable {
        MailService mailService;

        public PollMail(MailService mailService) {
            this.mailService = mailService;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Email mail = MailQueue.getMailQueue().consume();
                    if (mail != null) {
                        logger.info("剩余邮件总数:{}", MailQueue.getMailQueue().size());
                        mailService.send(mail);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
