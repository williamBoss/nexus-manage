package com.nexus.system.controller.system;

import com.nexus.common.domain.AjaxResult;
import com.nexus.common.utils.StringUtils;
import com.nexus.core.controller.BaseController;
import com.nexus.core.domain.dto.RegisterBody;
import com.nexus.system.service.ISysConfigService;
import com.nexus.system.service.impl.SysRegisterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册验证
 */
@RestController
public class SysRegisterController extends BaseController {

	private final SysRegisterService registerService;

	private final ISysConfigService configService;

	public SysRegisterController(SysRegisterService registerService, ISysConfigService configService) {
		this.registerService = registerService;
		this.configService = configService;
	}

	@PostMapping("/register")
	public AjaxResult register(@RequestBody RegisterBody user) {
		if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
			return error("当前系统没有开启注册功能！");
		}
		String msg = registerService.register(user);
		return StringUtils.isEmpty(msg) ? success() : error(msg);
	}
}
