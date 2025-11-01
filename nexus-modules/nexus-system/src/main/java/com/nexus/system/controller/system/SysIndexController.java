package com.nexus.system.controller.system;

import com.nexus.common.utils.StringUtils;
import com.nexus.core.properties.DefaultProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 */
@RestController
public class SysIndexController {

	/**
	 * 系统基础配置
	 */
	private final DefaultProperties defaultProperties;

	public SysIndexController(DefaultProperties defaultProperties) {
		this.defaultProperties = defaultProperties;
	}

	/**
	 * 访问首页，提示语
	 */
	@RequestMapping("/")
	public String index() {
		return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。",
			defaultProperties.getName(), defaultProperties.getVersion());
	}
}
