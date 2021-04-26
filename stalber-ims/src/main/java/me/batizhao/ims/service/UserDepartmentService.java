package me.batizhao.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.ims.domain.UserDepartment;

import java.util.List;

/**
 * 用户部门关联接口类
 *
 * @author batizhao
 * @since 2021-04-26
 */
public interface UserDepartmentService extends IService<UserDepartment> {

    /**
     * 更新用户部门关联
     * @param userDepartments 用户部门关联
     * @return Boolean
     */
    Boolean updateUserDepartments(List<UserDepartment> userDepartments);
}