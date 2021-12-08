package me.batizhao.ims.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.annotation.SystemLog;
import me.batizhao.common.constant.PecadoConstants;
import me.batizhao.common.util.R;
import me.batizhao.common.util.SecurityUtils;
import me.batizhao.ims.domain.*;
import me.batizhao.ims.service.UserDepartmentService;
import me.batizhao.ims.service.UserPostService;
import me.batizhao.ims.service.UserRoleService;
import me.batizhao.ims.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.List;

/**
 * 用户管理
 * 这里是用户管理接口的描述
 *
 * @author batizhao
 * @module pecado-ims
 * @since 2016/9/28
 */
@Tag(name = "用户管理")
@RestController
@Slf4j
@Validated
@RequestMapping("ims")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserPostService userPostService;
    @Autowired
    private UserDepartmentService userDepartmentService;

    /**
     * 分页查询
     *
     * @param page         分页对象
     * @param user         用户
     * @param departmentId 部门ID
     * @return 用户集合
     * @real_return R<Page < User>>
     */
    @Operation(description = "分页查询用户")
    @GetMapping("/users")
    @PreAuthorize("@pms.hasPermission('ims:user:admin')")
    public R<IPage<User>> handleUsers(Page<User> page, User user, Long departmentId) {
        return R.ok(userService.findUsers(user, page, departmentId));
    }

    /**
     * 查询用户
     * 返回用户集合
     *
     * @return R<List < User>>
     */
    @Operation(description = "查询用户")
    @GetMapping("/user")
    @PreAuthorize("@pms.hasPermission('ims:user:admin')")
    public R<List<User>> handleUsers(User user) {
        return R.ok(userService.findUsers(user));
    }

    /**
     * 通过id查询用户
     *
     * @param id id
     * @return R<User>
     */
    @Operation(description = "通过id查询用户")
    @GetMapping("/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public R<User> handleId(@Parameter(name = "ID", required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(userService.findById(id));
    }

    /**
     * 根据用户名查询用户
     * 用户名不重复，返回单个用户详情（包括其角色）
     *
     * @param username 用户名
     * @return R<UserInfoVO>
     */
    @Operation(description = "根据用户名查询用户")
    @GetMapping(value = "/user", params = "username")
    @PreAuthorize("isAuthenticated()")
    @SystemLog
    public R<UserInfoVO> handleUsername(@Parameter(name = "用户名", required = true) @RequestParam @Size(min = 3) String username) {
        User user = userService.findByUsername(username);
        return R.ok(userService.getUserInfo(user.getId()));
    }

    /**
     * 添加或编辑用户
     *
     * @param user 用户
     * @return R<User>
     */
    @Operation(description = "添加或编辑用户")
    @PostMapping("/user")
    @PreAuthorize("@pms.hasPermission('ims:user:add') or @pms.hasPermission('ims:user:edit')")
    @SystemLog
    public R<User> handleSaveOrUpdate(@Valid @Parameter(name = "用户", required = true) @RequestBody User user) {
        return R.ok(userService.saveOrUpdateUser(user));
    }

    /**
     * 删除用户
     * 根据用户ID删除用户
     *
     * @return R<Boolean>
     */
    @Operation(description = "删除用户")
    @DeleteMapping("/user")
    @PreAuthorize("@pms.hasPermission('ims:user:delete')")
    @SystemLog
    public R<Boolean> handleDelete(@Parameter(name = "用户ID串", required = true) @RequestParam List<Long> ids) {
        return R.ok(userService.deleteByIds(ids));
    }

    /**
     * 更新用户状态
     *
     * @param user 用户
     * @return R<Boolean>
     */
    @Operation(description = "更新用户状态")
    @PostMapping("/user/status")
    @PreAuthorize("@pms.hasPermission('ims:user:admin')")
    @SystemLog
    public R<Boolean> handleUpdateStatus(@Parameter(name = "用户", required = true) @RequestBody User user) {
        return R.ok(userService.updateStatus(user));
    }

    /**
     * 我的信息
     *
     * @return R<UserInfoVO>
     */
    @Operation(description = "我的信息")
    @GetMapping("/user/me")
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
    @Operation(description = "更换我的头像")
    @PostMapping("/user/avatar")
    @PreAuthorize("isAuthenticated()")
    @SystemLog
    public R<User> handleUpdateAvatar(@Parameter(name = "用户", required = true) @RequestBody User user) {
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
    @Operation(description = "更新我的密码")
    @PostMapping("/user/password")
    @PreAuthorize("isAuthenticated()")
    @SystemLog
    public R<Boolean> handleUpdatePassword(@Parameter(name = "旧密码", required = true) @Size(min = 6) @RequestParam String oldPassword,
                                           @Parameter(name = "新密码", required = true) @Size(min = 6) @RequestParam String newPassword) {
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
    @Operation(description = "分配用户角色")
    @PostMapping(value = "/user/role")
    @PreAuthorize("@pms.hasPermission('ims:user:admin')")
    @SystemLog
    public R<Boolean> handleAddUserRoles(@Parameter(name = "关联角色", required = true) @RequestBody List<UserRole> userRoleList) {
        return R.ok(userRoleService.updateUserRoles(userRoleList));
    }

    /**
     * 分配用户岗位
     * 返回 true or false
     *
     * @param userPosts 岗位清单
     * @return R<Boolean>
     */
    @Operation(description = "分配用户岗位")
    @PostMapping(value = "/user/post")
    @PreAuthorize("@pms.hasPermission('ims:user:admin')")
    @SystemLog
    public R<Boolean> handleAddUserPosts(@Parameter(name = "关联岗位", required = true) @RequestBody List<UserPost> userPosts) {
        return R.ok(userPostService.updateUserPosts(userPosts));
    }

    /**
     * 分配用户部门
     * 返回 true or false
     *
     * @param userDepartments 部门清单
     * @return R<Boolean>
     */
    @Operation(description = "分配用户部门")
    @PostMapping(value = "/user/department")
    @PreAuthorize("@pms.hasPermission('ims:user:admin')")
    @SystemLog
    public R<Boolean> handleAddUserDepartments(@Parameter(name = "关联部门", required = true) @RequestBody List<UserDepartment> userDepartments) {
        return R.ok(userDepartmentService.updateUserDepartments(userDepartments));
    }

    /**
     * 根据部门ID查询领导
     * 返回领导集合
     *
     * @param departmentId 部门ID
     * @return R<List < User>>
     */
    @Operation(description = "根据部门ID查询领导")
    @GetMapping(value = "/user/leader")
    @PreAuthorize("@pms.hasPermission('ims:user:admin')")
    public R<List<User>> handleLeadersByDepartmentId(@Parameter(name = "部门ID", required = true) @RequestParam("departmentId") @Min(1) Integer departmentId,
                                                     @Parameter(name = "领导类型") @RequestParam("type") String type) {
        return R.ok(userService.findLeadersByDepartmentId(departmentId, type));
    }

    /**
     * 查询登录用户的部门领导
     * 返回领导集合
     *
     * @return R<List < User>>
     */
    @Operation(description = "查询登录用户的部门领导")
    @GetMapping(value = "/user/dept/leader")
    @PreAuthorize("isAuthenticated()")
    public R<List<User>> handleLeadersByUserId() {
        return R.ok(userService.findLeaders());
    }

    /**
     * 导入
     * 返回状态标记
     *
     * @param file Excel文件
     * @param updateSupport 覆盖更新
     */
    @Operation(description = "导入")
    @PostMapping(value = "/user/import")
    @PreAuthorize("@pms.hasPermission('ims:user:import')")
    public R<String> handleImport(MultipartFile file, boolean updateSupport) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<User> users = reader.readAll(User.class);
        return R.ok(userService.importUsers(users, updateSupport));
    }

    /**
     * 导出
     * 返回Excel流
     *
     * @param user 过滤条件
     */
    @Operation(description = "导出")
    @PostMapping(value = "/user/export")
    @PreAuthorize("@pms.hasPermission('ims:user:export')")
    public void handleExport(Page<User> page, User user, Long departmentId, HttpServletResponse response) throws IOException {
        List<User> users = userService.findUsers(user, page, departmentId).getRecords();

        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(users, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                String.format("attachment; filename=%s.xlsx", PecadoConstants.BACK_END_PROJECT));

        ServletOutputStream out = response.getOutputStream();

        writer.flush(out, true);
        writer.close();
        IoUtil.close(out);
    }
}
