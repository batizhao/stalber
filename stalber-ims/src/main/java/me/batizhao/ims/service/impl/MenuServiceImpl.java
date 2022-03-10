package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.constant.MenuScopeEnum;
import me.batizhao.common.core.constant.MenuTypeEnum;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.common.core.util.TreeUtil;
import me.batizhao.ims.domain.Menu;
import me.batizhao.ims.domain.MetaVO;
import me.batizhao.ims.domain.RoleMenu;
import me.batizhao.ims.mapper.MenuMapper;
import me.batizhao.ims.service.MenuService;
import me.batizhao.ims.service.RoleMenuService;
import me.batizhao.ims.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<Menu> findMenusByRoleId(Long roleId) {
        List<Menu> menus = menuMapper.findMenusByRoleId(roleId);
        for (Menu menu : menus) {
            menu.setMeta(new MetaVO(menu.getName(), menu.getIcon(), true));
        }
        return menus;
    }

    @Override
    public Set<Menu> findMenusByUserId(Long userId) {
        Set<Menu> all = new HashSet<>();
        roleService.findRolesByUserId(userId).forEach(role -> all.addAll(findMenusByRoleId(role.getId())));
        return all;
    }

    @Override
    public List<Menu> findMenuTreeByUserId(Long userId, String scope) {
        return filterMenu(this.findMenusByUserId(userId), null, scope);
    }

    @Override
    public List<Menu> findMenuTree(Menu menu, String scope) {
        LambdaQueryWrapper<Menu> wrapper = Wrappers.lambdaQuery();
        if (null != menu && StringUtils.isNotBlank(menu.getName())) {
            wrapper.like(Menu::getName, menu.getName());
        }
        wrapper.eq(Menu::getScope, scope).orderByAsc(Menu::getSort);

        List<Menu> menus = menuMapper.selectList(wrapper);
        int min = menus.size() > 0 ? Collections.min(menus.stream().map(Menu::getPid).collect(Collectors.toList())) : 0;
        return TreeUtil.build(menus, min);
    }

    @Override
    public List<Menu> filterMenu(Set<Menu> all, Integer parentId, String scope) {
        List<Menu> menuTreeList = all.stream()
                .filter(menu -> MenuTypeEnum.MENU.getType().equals(menu.getType()))
                .filter(menu -> scope.equals(menu.getScope()))
                .sorted(Comparator.comparingInt(Menu::getSort))
                .collect(Collectors.toList());

        Integer parent = parentId == null ? 0 : parentId;
        return TreeUtil.build(menuTreeList, parent);
    }

    @Override
    public Menu findMenuById(Integer id) {
        Menu menu = menuMapper.selectById(id);
        if (menu == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }
        return menu;
    }

    @Override
    @Transactional
    public Menu saveOrUpdateMenu(Menu menu) {
        if (menu.getId() == null) {
            menu.setCreateTime(LocalDateTime.now());
            menu.setUpdateTime(LocalDateTime.now());
            menuMapper.insert(menu);
        } else {
            menu.setUpdateTime(LocalDateTime.now());
            menuMapper.updateById(menu);
        }

        return menu;
    }

    @Override
    @Transactional
    public Boolean deleteById(Integer id) {
        if (checkHasChildren(id)) return false;

        this.removeById(id);
        roleMenuService.remove(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getMenuId, id));
        return true;
    }

    @Override
    @Transactional
    public Boolean updateStatus(Menu menu) {
        LambdaUpdateWrapper<Menu> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Menu::getId, menu.getId()).set(Menu::getStatus, menu.getStatus());
        return menuMapper.update(null, wrapper) == 1;
    }

    @Override
    public Boolean checkHasChildren(Integer id) {
        return menuMapper.selectList(Wrappers.<Menu>lambdaQuery().eq(Menu::getPid, id)).size() > 0;
    }

    @Override
    public List<Menu> findMenusByAppId(Long appId) {
        LambdaQueryWrapper<Menu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Menu::getAppId, appId).eq(Menu::getScope, MenuScopeEnum.DASHBOARD.getType()).orderByAsc(Menu::getSort);

        List<Menu> menus = menuMapper.selectList(wrapper);
        int min = menus.size() > 0 ? Collections.min(menus.stream().map(Menu::getPid).collect(Collectors.toList())) : 1;
        return TreeUtil.build(menus, min);
    }
}
