package com.nexus.system.listener;

import com.nexus.core.event.OperLogEvent;
import com.nexus.system.service.ISysOperLogService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OperLogListener {

	private final ISysOperLogService operLogService;

	public OperLogListener(ISysOperLogService operLogService) {
		this.operLogService = operLogService;
	}

	@Async
	@EventListener
	public void handleOperLogEvent(OperLogEvent event) {
		operLogService.insertOperlog(event.getOperLog());
	}
}

