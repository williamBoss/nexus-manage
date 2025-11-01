package com.nexus.common.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Treeselect树结构实体类
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class TreeSelect implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 节点ID
	 */
	private Long id;

	/**
	 * 节点名称
	 */
	private String label;

	/**
	 * 节点禁用
	 */
	private boolean disabled = false;

	/**
	 * 子节点
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<TreeSelect> children;

}
