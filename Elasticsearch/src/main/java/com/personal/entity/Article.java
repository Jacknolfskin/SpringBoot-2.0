package com.personal.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Jacknolfskin on 2017/11/12.
 * indexName 配置必须是全部小写，不然会出异常
 */
@Data
@Document(indexName="article",type="articleType")
public class Article implements Serializable {

    @Id
    private Long id;

    /**标题*/
    @Field(type = FieldType.text, analyzer="ik")
    private String title;

    /**摘要*/
    private String abstracts;

    /**内容*/
    private String content;

    /**发表时间*/
    private Date postTime;

    /**点击率*/
    private Long clickCount;

    /**作者*/
    private Author author;

    /**所属教程*/
    private Tutorial tutorial;
}
