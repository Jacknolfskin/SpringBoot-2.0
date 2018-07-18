package com.personal.asyncsendmail.service;


import com.personal.asyncsendmail.entity.Email;


public interface MailService {

    /**
     * 发送邮件
     */
    public void send(Email mail) throws Exception;


    /**
     * 将发送邮件任务添加到队列
     */
    public void sendQueue(Email mail) throws Exception;

}
