package me.batizhao.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.ims.domain.RoleDepartment;

import java.util.List;

/**
 * 角色数据权限接口类
 *
 * @author batizhao
 * @since 2021-05-28
 */
public interface RoleDepartmentService extends IService<RoleDepartment> {

    /**
     * 更新角色数据权限
     * @param roleDepartments 角色部门关联
     * @return Boolean
     */
    Boolean updateRoleDepartments(List<RoleDepartment> roleDepartments);

}
