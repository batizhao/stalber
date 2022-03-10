package me.batizhao.common.core.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lengleng
 * @date 2020-02-17
 * <p>
 * 菜单类型
 */
@Getter
@RequiredArgsConstructor
public enum MenuTypeEnum {

	/**
	 * 左侧菜单
	 */
	MENU("M", "menu"),

	/**
	 * 顶部菜单
	 */
//	TOP_MENU("T", "top"),

	/**
	 * 按钮
	 */
	BUTTON("B", "button");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

}
