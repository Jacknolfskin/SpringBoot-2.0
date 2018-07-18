package com.personal.asyncsendmail.controller;

import com.personal.asyncsendmail.entity.Email;
import com.personal.asyncsendmail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * feihu5
 */
@RestController
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/sendMail")
    public String send(Email mail) {
        try {
            mailService.sendQueue(mail);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
        return "OK";
    }
}
