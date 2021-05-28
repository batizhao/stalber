package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.exception.NotFoundException;
import me.batizhao.common.exception.StalberException;
import me.batizhao.ims.domain.Role;
import me.batizhao.ims.domain.RoleDepartment;
import me.batizhao.ims.domain.RoleMenu;
import me.batizhao.ims.domain.UserRole;
import me.batizhao.ims.mapper.RoleMapper;
import me.batizhao.ims.service.RoleDepartmentService;
import me.batizhao.ims.service.RoleMenuService;
import me.batizhao.ims.service.RoleService;
import me.batizhao.ims.service.UserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private RoleDepartmentService roleDepartmentService;

    @Override
    public IPage<Role> findRoles(Page<Role> page, Role role) {
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(role.getName())) {
            wrapper.like(Role::getName, role.getName());
        }
        return roleMapper.selectPage(page, wrapper);
    }

    @Override
    public Role findById(Long id) {
        Role role = roleMapper.selectById(id);

        if(role == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return role;
    }

    @Override
    @Transactional
    public Role saveOrUpdateRole(Role role) {
        if (role.getId() == null) {
            role.setCreateTime(LocalDateTime.now());
            role.setUpdateTime(LocalDateTime.now());
            roleMapper.insert(role);
        } else {
            role.setUpdateTime(LocalDateTime.now());
            roleMapper.updateById(role);
        }

        return role;
    }

    @Override
    @Transactional
    public Boolean deleteByIds(List<Long> ids) {
        this.removeByIds(ids);
        ids.forEach(i -> {
            checkRoleIsSystem(i);
            userRoleService.remove(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId, i));
            roleMenuService.remove(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, i));
            roleDepartmentService.remove(Wrappers.<RoleDepartment>lambdaQuery().eq(RoleDepartment::getRoleId, i));
        });
        return true;
    }

    @Override
    @Transactional
    public Boolean updateStatus(Role role) {
        LambdaUpdateWrapper<Role> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Role::getId, role.getId()).set(Role::getStatus, role.getStatus());
        return roleMapper.update(null, wrapper) == 1;
    }

    @Override
    public List<Role> findRolesByUserId(Long userId) {
        return roleMapper.findRolesByUserId(userId);
    }

    @Override
    @Transactional
    public Boolean updateDataScope(Role role) {
        checkRoleIsSystem(role.getId());
        roleMapper.updateById(role);
        if (role.getDataScope().equals("custom")) {
            return roleDepartmentService.updateRoleDepartments(role.getRoleDepartments());
        }
        return true;
    }

    private void checkRoleIsSystem(Long id) {
        if (id.equals(1L) || id.equals(2L)) {
            throw new StalberException("Operation not allowed!");
        }
    }
}
