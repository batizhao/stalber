package me.batizhao.ims.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import me.batizhao.common.core.domain.IdName;

import java.io.Serializable;
import java.util.List;

/**
 * @author batizhao
 * @since 2020-05-09
 **/
@Data
@Accessors(chain = true)
@Schema(description = "用户信息")
public class UserInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "用户基本信息")
	private User user;

	/**
	 * @mock @pick(["common","admin"])
	 */
	@Schema(description = "权限清单")
	private List<String> permissions;

	/**
	 * @mock @pick(["ADMIN","MEMBER","GUEST"])
	 */
	@Schema(description = "角色清单")
	private List<String> roles;

	/**
	 * @mock @pick([1,2,3])
	 */
	@Schema(description = "角色ID串")
	private List<String> roleIds;

	/**
	 * @mock @pick([{id:1,name:xxx},{id:2,name:vvv}])
	 */
	@Schema(description = "部门集合")
	private List<IdName> departmentList;
}
