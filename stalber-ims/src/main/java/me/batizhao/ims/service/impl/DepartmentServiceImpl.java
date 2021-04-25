package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.exception.NotFoundException;
import me.batizhao.common.exception.StalberException;
import me.batizhao.common.util.TreeUtil;
import me.batizhao.ims.domain.Department;
import me.batizhao.ims.mapper.DepartmentMapper;
import me.batizhao.ims.service.DepartmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 部门接口实现类
 *
 * @author batizhao
 * @since 2021-04-25
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> findDepartmentTree(Department department) {
        LambdaQueryWrapper<Department> wrapper = Wrappers.lambdaQuery();
        if (null != department && StringUtils.isNotBlank(department.getName())) {
            wrapper.like(Department::getName, department.getName());
        }
        if (null != department && StringUtils.isNotBlank(department.getFullName())) {
            wrapper.like(Department::getFullName, department.getFullName());
        }
        wrapper.orderByAsc(Department::getSort);

        List<Department> departments = departmentMapper.selectList(wrapper);
        int min = departments.size() > 0 ? Collections.min(departments.stream().map(Department::getPid).collect(Collectors.toList())) : 0;
        return TreeUtil.build(departments, min);
    }

    @Override
    public Department findById(Long id) {
        Department department = departmentMapper.selectById(id);

        if(department == null) {
            throw new NotFoundException(String.format("没有该记录 '%s'。", id));
        }

        return department;
    }

    @Override
    @Transactional
    public Department saveOrUpdateDepartment(Department department) {
        if (department.getId() == null) {
            department.setCreateTime(LocalDateTime.now());
            department.setUpdateTime(LocalDateTime.now());
            department.setUuid(UUID.randomUUID().toString());
            departmentMapper.insert(department);
        } else {
            department.setUpdateTime(LocalDateTime.now());
            departmentMapper.updateById(department);
        }

        return department;
    }

    @Override
    @Transactional
    public Boolean deleteById(Integer id) {
        if (checkHasChildren(id)) return false;
        checkDepartmentIsRoot(id);
        this.removeById(id);
        return true;
    }

    private void checkDepartmentIsRoot(Integer id) {
        if (id.equals(1)) {
            throw new StalberException("不允许操作！");
        }
    }

    @Override
    @Transactional
    public Boolean updateStatus(Department department) {
        LambdaUpdateWrapper<Department> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Department::getId, department.getId()).set(Department::getStatus, department.getStatus());
        return departmentMapper.update(null, wrapper) == 1;
    }

    @Override
    public Boolean checkHasChildren(Integer id) {
        return departmentMapper.selectList(Wrappers.<Department>lambdaQuery().eq(Department::getPid, id)).size() > 0;
    }
}
