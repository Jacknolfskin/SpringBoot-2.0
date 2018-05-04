package com.personal.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * Created by Jacknolfskin on 2017/11/12.
 */
@Data
@Document(indexName = "account",type = "accountType", shards = 1,replicas = 0, refreshInterval = "-1")
public class AccountInfo {
    @Id
    private String id;
    @Field
    private String accountName;
    @Field
    private String nickName;
}
