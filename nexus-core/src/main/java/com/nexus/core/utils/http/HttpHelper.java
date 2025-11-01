package com.nexus.core.utils.http;

import jakarta.servlet.ServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 通用http工具封装
 */
@Slf4j
public class HttpHelper {

	public static String getBodyString(ServletRequest request) {
		StringBuilder sb = new StringBuilder();
		try (InputStream inputStream = request.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			log.error("getBodyString出现问题！");
		}
		return sb.toString();
	}
}
