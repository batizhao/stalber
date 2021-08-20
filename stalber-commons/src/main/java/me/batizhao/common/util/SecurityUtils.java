/*
 *
 *  *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  *  <p>
 *  *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  * https://www.gnu.org/licenses/lgpl.html
 *  *  <p>
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package me.batizhao.common.util;

import lombok.experimental.UtilityClass;
import me.batizhao.common.constant.SecurityConstants;
import me.batizhao.common.domain.PecadoUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 安全工具类
 *
 * @author L.cm
 */
@UtilityClass
public class SecurityUtils {

	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户
	 */
	public PecadoUser getUser(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		if (principal instanceof Jwt) {
			Jwt jwt = (Jwt) principal;
			Long userId = jwt.getClaim(SecurityConstants.DETAILS_USER_ID);
			List<Integer> deptIds = jwt.getClaim(SecurityConstants.DETAILS_DEPT_ID);
			List<Long> roleIds = jwt.getClaim(SecurityConstants.DETAILS_ROLE_ID);
			String username = jwt.getClaimAsString(SecurityConstants.DETAILS_USERNAME);
			List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(
					StringUtils.collectionToCommaDelimitedString(jwt.getClaim(SecurityConstants.DETAILS_AUTHORITIES)));

			return new PecadoUser(userId, deptIds, roleIds, username, "", true, true, true, true, authorities);
		}
		return null;
	}

	/**
	 * 获取用户
	 */
	public PecadoUser getUser() {
		Authentication authentication = getAuthentication();
		if (authentication == null) {
			return null;
		}
		return getUser(authentication);
	}

}
