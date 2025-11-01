package com.nexus.system.listener;

import com.nexus.core.event.LoginLogEvent;
import com.nexus.system.service.ISysLogininforService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 登录日志监听
 */
@Component
public class LoginLogListener {

	private final ISysLogininforService logininforService;

	public LoginLogListener(ISysLogininforService logininforService) {
		this.logininforService = logininforService;
	}

	@Async
	@EventListener
	public void handleLoginLogEvent(LoginLogEvent event) {
		logininforService.insertLogininfor(event.getLogininfor());
	}
}
