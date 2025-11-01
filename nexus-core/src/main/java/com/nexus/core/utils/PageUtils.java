package com.nexus.core.utils;

import com.github.pagehelper.PageHelper;
import com.nexus.common.domain.PageDTO;
import com.nexus.common.utils.sql.SqlUtil;
import com.nexus.core.page.TableSupport;

/**
 * 分页工具类
 */
public class PageUtils extends PageHelper {

	/**
	 * 设置请求分页数据
	 */
	public static void startPage() {
		PageDTO pageDTO = TableSupport.buildPageRequest();
		Integer pageNum = pageDTO.getPageNum();
		Integer pageSize = pageDTO.getPageSize();
		String orderBy = SqlUtil.escapeOrderBySql(pageDTO.getOrderBy());
		Boolean reasonable = pageDTO.getReasonable();
		PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
	}

	/**
	 * 清理分页的线程变量
	 */
	public static void clearPage() {
		PageHelper.clearPage();
	}

}
