/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.batizhao.admin.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;

/**
 * @author lengleng
 * @date 2019/2/1 接口权限判断工具
 */
@Slf4j
@Component("pms")
public class PermissionService {

	/**
	 * 判断接口是否有xxx:xxx权限
	 * @param permission 权限
	 * @return {boolean}
	 */
	public boolean hasPermission(String permission) {
		if (StringUtils.isBlank(permission)) {
			return false;
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		return authorities.stream().map(GrantedAuthority::getAuthority).filter(org.springframework.util.StringUtils::hasText)
				.anyMatch(x -> PatternMatchUtils.simpleMatch(permission, x));
	}

}
