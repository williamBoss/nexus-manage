package com.nexus.system.controller.monitor;

import com.nexus.common.domain.AjaxResult;
import com.nexus.common.enums.BusinessType;
import com.nexus.core.annotation.Log;
import com.nexus.core.controller.BaseController;
import com.nexus.core.page.TableDataInfo;
import com.nexus.core.utils.poi.ExcelUtil;
import com.nexus.core.domain.entity.SysOperLog;
import com.nexus.system.service.ISysOperLogService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志记录
 */
@RestController
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController {

	private final ISysOperLogService operLogService;

	public SysOperlogController(ISysOperLogService operLogService) {
		this.operLogService = operLogService;
	}

	@PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
	@GetMapping("/list")
	public TableDataInfo list(SysOperLog operLog) {
		startPage();
		List<SysOperLog> list = operLogService.selectOperLogList(operLog);
		return getDataTable(list);
	}

	@Log(title = "操作日志", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('monitor:operlog:export')")
	@PostMapping("/export")
	public void export(HttpServletResponse response, SysOperLog operLog) {
		List<SysOperLog> list = operLogService.selectOperLogList(operLog);
		ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
		util.exportExcel(response, list, "操作日志");
	}

	@Log(title = "操作日志", businessType = BusinessType.DELETE)
	@PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
	@DeleteMapping("/{operIds}")
	public AjaxResult remove(@PathVariable Long[] operIds) {
		return toAjax(operLogService.deleteOperLogByIds(operIds));
	}

	@Log(title = "操作日志", businessType = BusinessType.CLEAN)
	@PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
	@DeleteMapping("/clean")
	public AjaxResult clean() {
		operLogService.cleanOperLog();
		return success();
	}
}
