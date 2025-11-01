package com.nexus.system.controller.system;

import com.nexus.common.constant.Constants;
import com.nexus.common.domain.AjaxResult;
import com.nexus.common.utils.DateUtils;
import com.nexus.common.utils.StringUtils;
import com.nexus.common.utils.text.Convert;
import com.nexus.core.domain.dto.LoginBody;
import com.nexus.core.domain.dto.LoginUser;
import com.nexus.core.domain.entity.SysMenu;
import com.nexus.core.domain.entity.SysUser;
import com.nexus.core.security.token.TokenManager;
import com.nexus.core.utils.SecurityUtils;
import com.nexus.system.service.ISysConfigService;
import com.nexus.system.service.ISysLoginService;
import com.nexus.system.service.ISysMenuService;
import com.nexus.system.service.impl.SysPermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 登录验证
 */
@RestController
public class SysLoginController {

	private final ISysLoginService loginService;

	private final ISysMenuService menuService;

	private final SysPermissionService permissionService;

	private final TokenManager tokenManager;

	private final ISysConfigService configService;

	public SysLoginController(ISysLoginService loginService, ISysMenuService menuService,
		SysPermissionService permissionService, TokenManager tokenManager, ISysConfigService configService) {
		this.loginService = loginService;
		this.menuService = menuService;
		this.permissionService = permissionService;
		this.tokenManager = tokenManager;
		this.configService = configService;
	}

	/**
	 * 登录方法
	 *
	 * @param loginBody 登录信息
	 * @return 结果
	 */
	@PostMapping("/login")
	public AjaxResult login(@RequestBody LoginBody loginBody) {
		AjaxResult ajax = AjaxResult.success();
		// 生成令牌
		String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
			loginBody.getUuid());
		ajax.put(Constants.TOKEN, token);
		return ajax;
	}

	/**
	 * 获取用户信息
	 *
	 * @return 用户信息
	 */
	@GetMapping("getInfo")
	public AjaxResult getInfo() {
		LoginUser loginUser = SecurityUtils.getLoginUser();
		SysUser user = loginUser.getUser();
		// 角色集合
		Set<String> roles = permissionService.getRolePermission(user);
		// 权限集合
		Set<String> permissions = permissionService.getMenuPermission(user);
		if (!loginUser.getPermissions().equals(permissions)) {
			loginUser.setPermissions(permissions);
			tokenManager.refreshToken(loginUser);
		}
		AjaxResult ajax = AjaxResult.success();
		ajax.put("user", user);
		ajax.put("roles", roles);
		ajax.put("permissions", permissions);
		ajax.put("isDefaultModifyPwd", initPasswordIsModify(user.getPwdUpdateDate()));
		ajax.put("isPasswordExpired", passwordIsExpiration(user.getPwdUpdateDate()));
		return ajax;
	}

	/**
	 * 获取路由信息
	 *
	 * @return 路由信息
	 */
	@GetMapping("getRouters")
	public AjaxResult getRouters() {
		Long userId = SecurityUtils.getUserId();
		List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
		return AjaxResult.success(menuService.buildMenus(menus));
	}

	// 检查初始密码是否提醒修改
	public boolean initPasswordIsModify(Date pwdUpdateDate) {
		Integer initPasswordModify = Convert.toInt(configService.selectConfigByKey("sys.account.initPasswordModify"));
		return initPasswordModify != null && initPasswordModify == 1 && pwdUpdateDate == null;
	}

	// 检查密码是否过期
	public boolean passwordIsExpiration(Date pwdUpdateDate) {
		Integer passwordValidateDays =
			Convert.toInt(configService.selectConfigByKey("sys.account.passwordValidateDays"));
		if (passwordValidateDays != null && passwordValidateDays > 0) {
			if (StringUtils.isNull(pwdUpdateDate)) {
				// 如果从未修改过初始密码，直接提醒过期
				return true;
			}
			Date nowDate = DateUtils.getNowDate();
			return DateUtils.differentDaysByMillisecond(nowDate, pwdUpdateDate) > passwordValidateDays;
		}
		return false;
	}
}
