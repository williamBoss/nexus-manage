package com.nexus.core.utils.ip;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.nexus.common.constant.Constants;
import com.nexus.common.utils.StringUtils;
import com.nexus.core.properties.DefaultProperties;
import com.nexus.core.utils.http.HttpUtils;
import com.nexus.core.utils.spring.SpringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取地址类
 */
@Slf4j
public class AddressUtils {
	// IP地址查询
	public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";
	// 未知地址
	public static final String UNKNOWN = "XX XX";
	@Resource
	private DefaultProperties defaultProperties;

	public static String getRealAddressByIP(String ip) {
		// 内网不查询
		if (IpUtils.internalIp(ip)) {
			return "内网IP";
		}
		if (SpringUtils.getBean(DefaultProperties.class).getAddressEnabled()) {
			try {
				String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", Constants.GBK);
				if (StringUtils.isEmpty(rspStr)) {
					log.error("获取地理位置异常 {}", ip);
					return UNKNOWN;
				}
				JSONObject obj = JSON.parseObject(rspStr);
				String region = obj.getString("pro");
				String city = obj.getString("city");
				return String.format("%s %s", region, city);
			} catch (Exception e) {
				log.error("获取地理位置异常 {}", ip);
			}
		}
		return UNKNOWN;
	}
}
