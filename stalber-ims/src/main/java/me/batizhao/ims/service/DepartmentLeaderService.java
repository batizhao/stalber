package me.batizhao.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.ims.domain.DepartmentLeader;

import java.util.List;

/**
 * 部门领导关联接口类
 *
 * @author batizhao
 * @since 2021-04-26
 */
public interface DepartmentLeaderService extends IService<DepartmentLeader> {

    /**
     * 更新部门领导关联状态
     * @param departmentLeaders 部门领导关联
     * @return Boolean
     */
    Boolean updateDepartmentLeaders(List<DepartmentLeader> departmentLeaders);
}