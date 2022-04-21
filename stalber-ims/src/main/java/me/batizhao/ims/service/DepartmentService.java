package me.batizhao.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.ims.domain.Department;

import java.util.List;

/**
 * 部门接口类
 *
 * @author batizhao
 * @since 2021-04-25
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 查询所有部门
     * @return 部门树
     */
    List<Department> findDepartmentTree(Department department);

    /**
     * 通过id查询部门
     * @param id id
     * @return Department
     */
    Department findById(Long id);

    /**
     * 添加或编辑部门
     * @param department 部门
     * @return Department
     */
    Department saveOrUpdateDepartment(Department department);

    /**
     * 删除
     * @param id
     * @return
     */
    Boolean deleteById(Integer id);

    /**
     * 更新部门状态
     * @param department 部门
     * @return Boolean
     */
    Boolean updateStatus(Department department);

    /**
     * 检查是否有子部门
     * 有返回 true，无返回 false
     *
     * @param id
     * @return
     */
    Boolean checkHasChildren(Integer id);

    /**
     * 通过用户 ID 查相关的部门
     * @param userId
     * @return
     */
    List<Department> findDepartmentsByUserId(Long userId);

    /**
     * 通过角色 ID 查相关的部门
     * @param roleId
     * @return
     */
    List<Department> findDepartmentsByRoleId(Long roleId);

    /**
     * 通过 Level 查相关的部门
     * @param level
     * @return
     */
    List<Department> findByLevel(String level);
}
