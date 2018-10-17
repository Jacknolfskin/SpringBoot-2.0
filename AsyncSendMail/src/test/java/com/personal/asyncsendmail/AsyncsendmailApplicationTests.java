package com.personal.asyncsendmail;

import com.personal.asyncsendmail.entity.Email;
import com.personal.asyncsendmail.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncsendmailApplicationTests {

    @Autowired
    private MailService mailService;
    @Test
    public void contextLoads() {
        Email email = new Email();
        email.setContent("fdafdf");
        email.setEmail(new String[]{"feihu5@iflytek.com"});
        email.setSubject("你好");
        try {
            mailService.send(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
