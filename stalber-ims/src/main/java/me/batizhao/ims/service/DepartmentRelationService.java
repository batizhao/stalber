package me.batizhao.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.ims.domain.Department;
import me.batizhao.ims.domain.DepartmentRelation;

/**
 * 部门依赖关系接口类
 *
 * @author batizhao
 * @since 2021-04-29
 */
public interface DepartmentRelationService extends IService<DepartmentRelation> {

    /**
     * 添加部门依赖关系
     * @param department 部门
     * @return Boolean
     */
    Boolean saveDepartmentRelation(Department department);

    /**
     * 更新部门依赖关系
     * @param department 部门
     * @return Boolean
     */
    Boolean updateDepartmentRelation(Department department);
}