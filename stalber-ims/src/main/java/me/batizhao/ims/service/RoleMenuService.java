package me.batizhao.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.ims.domain.RoleMenu;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-09-14
 **/
public interface RoleMenuService extends IService<RoleMenu> {

    /**
     * 更新角色菜单
     * @param roleMenuList
     * @return
     */
    Boolean updateRoleMenus(List<RoleMenu> roleMenuList);

}
