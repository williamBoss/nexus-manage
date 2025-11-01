package com.nexus.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取IP地址
 */
@Slf4j
public class IpUtils {

	/**
	 * 获取IP地址
	 *
	 * @return 本地IP地址
	 */
	public static String getHostIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.error("获取IP地址异常", e);
		}
		return "127.0.0.1";
	}

	/**
	 * 获取主机名
	 *
	 * @return 本地主机名
	 */
	public static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			log.error("获取主机名异常", e);
		}
		return "未知";
	}
}
