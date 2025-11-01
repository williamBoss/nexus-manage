package com.nexus.system.controller.monitor;

import com.nexus.common.domain.AjaxResult;
import com.nexus.common.enums.BusinessType;
import com.nexus.core.annotation.Log;
import com.nexus.core.controller.BaseController;
import com.nexus.core.domain.entity.SysLogininfor;
import com.nexus.core.page.TableDataInfo;
import com.nexus.core.utils.poi.ExcelUtil;
import com.nexus.system.service.ISysLogininforService;
import com.nexus.system.service.impl.SysPasswordService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统访问记录
 */
@RestController
@RequestMapping("/monitor/logininfor")
public class SysLogininforController extends BaseController {

	private final ISysLogininforService logininforService;

	private final SysPasswordService passwordService;

	public SysLogininforController(ISysLogininforService logininforService, SysPasswordService passwordService) {
		this.logininforService = logininforService;
		this.passwordService = passwordService;
	}

	@PreAuthorize("@ss.hasPermi('monitor:logininfor:list')")
	@GetMapping("/list")
	public TableDataInfo list(SysLogininfor logininfor) {
		startPage();
		List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
		return getDataTable(list);
	}

	@Log(title = "登录日志", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('monitor:logininfor:export')")
	@PostMapping("/export")
	public void export(HttpServletResponse response, SysLogininfor logininfor) {
		List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
		ExcelUtil<SysLogininfor> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
		util.exportExcel(response, list, "登录日志");
	}

	@PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
	@Log(title = "登录日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{infoIds}")
	public AjaxResult remove(@PathVariable Long[] infoIds) {
		return toAjax(logininforService.deleteLogininforByIds(infoIds));
	}

	@PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
	@Log(title = "登录日志", businessType = BusinessType.CLEAN)
	@DeleteMapping("/clean")
	public AjaxResult clean() {
		logininforService.cleanLogininfor();
		return success();
	}

	@PreAuthorize("@ss.hasPermi('monitor:logininfor:unlock')")
	@Log(title = "账户解锁", businessType = BusinessType.OTHER)
	@GetMapping("/unlock/{userName}")
	public AjaxResult unlock(@PathVariable("userName") String userName) {
		passwordService.clearLoginRecordCache(userName);
		return success();
	}
}
