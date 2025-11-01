package com.nexus.system.controller.monitor;

import com.nexus.common.domain.AjaxResult;
import com.nexus.common.domain.ServerInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器监控
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController {
	@PreAuthorize("@ss.hasPermi('monitor:server:list')")
	@GetMapping()
	public AjaxResult getInfo() throws Exception {
		ServerInfo server = new ServerInfo();
		server.copyTo();
		return AjaxResult.success(server);
	}
}
