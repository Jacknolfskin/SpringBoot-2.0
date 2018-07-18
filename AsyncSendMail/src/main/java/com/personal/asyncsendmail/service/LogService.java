package com.personal.asyncsendmail.service;

import com.personal.asyncsendmail.dao.LogRepo;
import com.personal.asyncsendmail.entity.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * LogService
 */
@Service
public class LogService extends BaseService<Log, LogRepo> {

    @Autowired
    public LogService(LogRepo repo) {
        super(repo);
    }

    public Log findOldLog(Long commonId, String type, String content) {
        return repo.findByCommonIdAndTypeAndContent(commonId, type, content);
    }
}