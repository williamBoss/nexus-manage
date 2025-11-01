package com.nexus.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 项目默认配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "default.project")
public class DefaultProperties {

	/**
	 * 上传路径
	 */
	private String profile;

	/**
	 * 获取地址开关
	 */
	private Boolean addressEnabled;

	/**
	 * 验证码类型
	 */
	private String captchaType;

	/**
	 * 项目名称
	 */
	private String name;

	/**
	 * 版本
	 */
	private String version;

	/**
	 * 版权年份
	 */
	private String copyrightYear;

}
