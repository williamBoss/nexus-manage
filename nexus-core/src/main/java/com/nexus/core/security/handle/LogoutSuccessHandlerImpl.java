package com.nexus.core.security.handle;

import com.alibaba.fastjson2.JSON;
import com.nexus.common.constant.Constants;
import com.nexus.common.domain.AjaxResult;
import com.nexus.common.utils.StringUtils;
import com.nexus.core.domain.dto.LoginUser;
import com.nexus.core.manager.AsyncManager;
import com.nexus.core.manager.factory.AsyncFactory;
import com.nexus.core.security.token.TokenManager;
import com.nexus.core.utils.MessageUtils;
import com.nexus.core.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

	private final TokenManager tokenManager;

	public LogoutSuccessHandlerImpl(TokenManager tokenManager) {
		this.tokenManager = tokenManager;
	}

	/**
	 * 退出处理
	 *
	 * @return
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
		throws IOException, ServletException {
		LoginUser loginUser = tokenManager.getLoginUser(request);
		if (StringUtils.isNotNull(loginUser)) {
			String userName = loginUser.getUsername();
			// 删除用户缓存记录
			tokenManager.delLoginUser(loginUser.getToken());
			// 记录用户退出日志
			AsyncManager.me()
				.execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT,
					MessageUtils.message("user.logout.success")));
		}
		ServletUtils.renderString(response,
			JSON.toJSONString(AjaxResult.success(MessageUtils.message("user.logout.success"))));
	}
}
