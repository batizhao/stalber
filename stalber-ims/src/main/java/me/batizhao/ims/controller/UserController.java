package me.batizhao.ims.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.annotation.SystemLog;
import me.batizhao.common.util.R;
import me.batizhao.common.util.SecurityUtils;
import me.batizhao.ims.domain.User;
import me.batizhao.ims.domain.UserInfoVO;
import me.batizhao.ims.domain.UserRole;
import me.batizhao.ims.service.UserRoleService;
import me.batizhao.ims.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 用户管理
 * 这里是用户管理接口的描述
 *
 * @module pecado-ims
 *
 * @author batizhao
 * @since 2016/9/28
 */
@Api(tags = "用户管理")
@RestController
@Slf4j
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param user 用户
     * @return 用户集合
     * @real_return R<Page<User>>
     */
    @ApiOperation(value = "分页查询用户")
    @GetMapping("/ims/users")
    @PreAuthorize("@pms.hasPermission('ims:user:admin')")
    public R<IPage<User>> handleUsers(Page<User> page, User user) {
        return R.ok(userService.findUsers(page, user));
    }

    /**
     * 通过id查询用户
     * @param id id
     * @return R<User>
     */
    @ApiOperation(value = "通过id查询用户")
    @GetMapping("/ims/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public R<User> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(userService.findById(id));
    }

    /**
     * 根据用户名查询用户
     * 用户名不重复，返回单个用户详情（包括其角色）
     *
     * @param username 用户名
     * @return R<UserInfoVO>
     */
    @ApiOperation(value = "根据用户名查询用户")
    @GetMapping(value = "/ims/user", params = "username")
    @PreAuthorize("isAuthenticated()")
    @SystemLog
    public R<UserInfoVO> handleUsername(@ApiParam(value = "用户名", required = true) @RequestParam @Size(min = 3) String username) {
        User user = userService.findByUsername(username);
        return R.ok(userService.getUserInfo(user.getId()));
    }

    /**
     * 添加或编辑用户
     * @param user 用户
     * @return R<User>
     */
    @ApiOperation(value = "添加或编辑用户")
    @PostMapping("/ims/user")
    @PreAuthorize("@pms.hasPermission('ims:user:add') or @pms.hasPermission('ims:user:edit')")
    @SystemLog
    public R<User> handleSaveOrUpdate(@Valid @ApiParam(value = "用户" , required = true) @RequestBody User user) {
        return R.ok(userService.saveOrUpdateUser(user));
    }

    /**
     * 删除用户
     * 根据用户ID删除用户
     *
     * @return R<Boolean>
     */
    @ApiOperation(value = "删除用户")
    @DeleteMapping("/ims/user")
    @PreAuthorize("@pms.hasPermission('ims:user:delete')")
    @SystemLog
    public R<Boolean> handleDelete(@ApiParam(value = "用户ID串", required = true) @RequestParam List<Long> ids) {
        return R.ok(userService.deleteByIds(ids));
    }

    /**
     * 更新用户状态
     *
     * @param user 用户
     * @return R<Boolean>
     */
    @ApiOperation(value = "更新用户状态")
    @PostMapping("/ims/user/status")
    @PreAuthorize("@pms.hasPermission('ims:user:admin')")
    @SystemLog
    public R<Boolean> handleUpdateStatus(@ApiParam(value = "用户" , required = true) @RequestBody User user) {
        return R.ok(userService.updateStatus(user));
    }

    /**
     * 我的信息
     *
     * @return R<UserInfoVO>
     */
    @ApiOperation(value = "我的信息")
    @GetMapping("/ims/user/me")
    @PreAuthorize("isAuthenticated()")
    public R<UserInfoVO> handleUserInfo() {
        Long userId = SecurityUtils.getUser().getUserId();
        return R.ok(userService.getUserInfo(userId));
    }

    /**
     * 更换我的头像
     *
     * @param user 用户
     * @return R<User>
     */
    @ApiOperation(value = "更换我的头像")
    @PostMapping("/ims/user/avatar")
    @PreAuthorize("isAuthenticated()")
    @SystemLog
    public R<User> handleUpdateAvatar(@ApiParam(value = "用户" , required = true) @RequestBody User user) {
        Long userId = SecurityUtils.getUser().getUserId();
        return R.ok(userService.saveOrUpdateUser(user.setId(userId)));
    }

    /**
     * 更新我的密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return R<Boolean>
     */
    @ApiOperation(value = "更新我的密码")
    @PostMapping("/ims/user/password")
    @PreAuthorize("isAuthenticated()")
    @SystemLog
    public R<Boolean> handleUpdatePassword(@ApiParam(value = "旧密码" , required = true) @Size(min = 6) @RequestParam String oldPassword,
                                           @ApiParam(value = "新密码" , required = true) @Size(min = 6) @RequestParam String newPassword) {
        Long userId = SecurityUtils.getUser().getUserId();
        return R.ok(userService.updatePassword(userId, oldPassword, newPassword));
    }

    /**
     * 分配用户角色
     * 返回 true or false
     *
     * @param userRoleList 角色清单
     * @return R<Boolean>
     */
    @ApiOperation(value = "分配用户角色")
    @PostMapping(value = "/ims/user/role")
    @PreAuthorize("@pms.hasPermission('ims:user:admin')")
    @SystemLog
    public R<Boolean> handleAddUserRoles(@ApiParam(value = "关联角色", required = true) @RequestBody List<UserRole> userRoleList) {
        return R.ok(userRoleService.updateUserRoles(userRoleList));
    }

}
