package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.exception.NotFoundException;
import me.batizhao.common.exception.StalberException;
import me.batizhao.ims.domain.*;
import me.batizhao.ims.mapper.UserMapper;
import me.batizhao.ims.service.MenuService;
import me.batizhao.ims.service.RoleService;
import me.batizhao.ims.service.UserRoleService;
import me.batizhao.ims.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public IPage<User> findUsers(Page<User> page, User user, Long departmentId) {
        if (departmentId == null) departmentId = 1L;
        return userMapper.selectUsers(page, user, departmentId);
    }

    @Override
    public List<User> findUsers(User user) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(user.getName())) {
            wrapper.like(User::getName, user.getName());
        }
        return userMapper.selectList(wrapper);
    }

    @Override
    public User findById(Long id) {
        User user = userMapper.selectById(id);

        if(user == null) {
            throw new NotFoundException(String.format("没有该记录 '%s'。", id));
        }

        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));

        if(user == null) {
            throw new NotFoundException(String.format("没有该用户 '%s'。", username));
        }

        return user;
    }

//    @Override
//    public List<User> findByName(String name) {
//        return userMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getName, name));
//    }

    @Override
    @Transactional
    public User saveOrUpdateUser(User user) {
        if (user.getId() == null) {
            BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
            String hashPass = bcryptPasswordEncoder.encode("123456");
            user.setPassword(hashPass);

            user.setUuid(UUID.randomUUID().toString());
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            userMapper.insert(user);
        } else {
            user.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(user);
        }

        return user;
    }

    @Override
    @Transactional
    public Boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = this.findById(userId);

        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bcryptPasswordEncoder.matches(oldPassword, user.getPassword()))
            throw new StalberException("旧密码不正确！");

        if (bcryptPasswordEncoder.matches(newPassword, user.getPassword()))
            throw new StalberException("新旧密码相同！");

        String hashPass = bcryptPasswordEncoder.encode(newPassword);
        user.setPassword(hashPass);
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.updateById(user) == 1;
    }

    @Override
    @Transactional
    public Boolean deleteByIds(List<Long> ids) {
        ids.forEach(i -> {
            checkUserIsAdmin(i);
            userRoleService.remove(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, i));
        });
        this.removeByIds(ids);
        return true;
    }

    private void checkUserIsAdmin(Long id) {
        if (id.equals(1L)) {
            throw new StalberException("管理员不允许操作！");
        }
    }

    @Override
    @Transactional
    public Boolean updateStatus(User user) {
        LambdaUpdateWrapper<User> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(User::getId, user.getId()).set(User::getStatus, user.getStatus());
        return userMapper.update(null, wrapper) == 1;
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);

        if(user == null) {
            throw new NotFoundException(String.format("没有该用户 '%s'。", userId));
        }

        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUser(user);
        userInfoVO.setRoles(roleService.findRolesByUserId(userId).stream().map(Role::getCode).collect(Collectors.toList()));
        userInfoVO.setPermissions(menuService.findMenusByUserId(userId).stream().map(Menu::getPermission).filter(org.springframework.util.StringUtils::hasText).collect(Collectors.toList()));
        return userInfoVO;
    }

    @Override
    public List<User> findLeadersByDepartmentId(Long departmentId, String type) {
        return userMapper.selectLeadersByDepartmentId(departmentId, type);
    }

}
