package com.nexus.system.controller.monitor;

import com.nexus.common.constant.CacheConstants;
import com.nexus.common.domain.AjaxResult;
import com.nexus.common.enums.BusinessType;
import com.nexus.common.utils.StringUtils;
import com.nexus.core.annotation.Log;
import com.nexus.core.controller.BaseController;
import com.nexus.core.domain.dto.LoginUser;
import com.nexus.core.page.TableDataInfo;
import com.nexus.core.utils.redis.RedisCache;
import com.nexus.core.domain.entity.SysUserOnline;
import com.nexus.system.service.ISysUserOnlineService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 在线用户监控
 */
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController {

	private final ISysUserOnlineService userOnlineService;

	private final RedisCache redisCache;

	public SysUserOnlineController(ISysUserOnlineService userOnlineService, RedisCache redisCache) {
		this.userOnlineService = userOnlineService;
		this.redisCache = redisCache;
	}

	@PreAuthorize("@ss.hasPermi('monitor:online:list')")
	@GetMapping("/list")
	public TableDataInfo list(String ipaddr, String userName) {
		Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
		List<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
		for (String key : keys) {
			LoginUser user = redisCache.getCacheObject(key);
			if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
				userOnlineList.add(userOnlineService.selectOnlineByInfo(ipaddr, userName, user));
			} else if (StringUtils.isNotEmpty(ipaddr)) {
				userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipaddr, user));
			} else if (StringUtils.isNotEmpty(userName) && StringUtils.isNotNull(user.getUser())) {
				userOnlineList.add(userOnlineService.selectOnlineByUserName(userName, user));
			} else {
				userOnlineList.add(userOnlineService.loginUserToUserOnline(user));
			}
		}
		Collections.reverse(userOnlineList);
		userOnlineList.removeAll(Collections.singleton(null));
		return getDataTable(userOnlineList);
	}

	/**
	 * 强退用户
	 */
	@PreAuthorize("@ss.hasPermi('monitor:online:forceLogout')")
	@Log(title = "在线用户", businessType = BusinessType.FORCE)
	@DeleteMapping("/{tokenId}")
	public AjaxResult forceLogout(@PathVariable String tokenId) {
		redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
		return success();
	}
}
