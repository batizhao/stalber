package me.batizhao.common.core.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author batizhao
 * @date 2022-03-10
 * <p>
 * 菜单范围
 */
@Getter
@RequiredArgsConstructor
public enum MenuScopeEnum {

	/**
	 * 左侧菜单
	 */
	DASHBOARD("dashboard", "工作台"),

	/**
	 * 按钮
	 */
	ADMIN("admin", "控制台");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

}
