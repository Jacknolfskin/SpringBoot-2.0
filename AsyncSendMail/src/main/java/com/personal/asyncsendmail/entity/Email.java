package com.personal.asyncsendmail.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * feihu5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 接收方邮件
	 */
	private String[] email;
	/**
	 * 主题
	 */
	private String subject;
	/**
	 * 邮件内容
	 */
	private String content;
}
