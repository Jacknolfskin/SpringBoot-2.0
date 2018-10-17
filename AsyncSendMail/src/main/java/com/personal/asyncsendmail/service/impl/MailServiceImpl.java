package com.personal.asyncsendmail.service.impl;

import com.personal.asyncsendmail.entity.Email;
import com.personal.asyncsendmail.queue.MailQueue;
import com.personal.asyncsendmail.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送者
     */
    @Value("${spring.mail.username}")
    public String USER_NAME;


    @Override
    public void send(Email mail) throws Exception {
        logger.info("发送邮件：{}", mail.getContent());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(USER_NAME);
        message.setTo(mail.getEmail());
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
        mailSender.send(message);
        logger.info("邮件已发送" +
                "：{}", mail.getEmail());
    }


    @Override
    public void sendQueue(Email mail) throws Exception {
        logger.info("发送邮件任务添加到队列：{}", mail.getContent());
        MailQueue.getMailQueue().produce(mail);
    }
}
