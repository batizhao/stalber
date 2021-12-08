package me.batizhao.ims.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.util.R;
import me.batizhao.ims.domain.Post;
import me.batizhao.ims.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 岗位 API
 *
 * @module ims
 *
 * @author batizhao
 * @since 2021-04-22
 */
@Tag(name = "岗位管理")
@RestController
@Slf4j
@Validated
@RequestMapping("ims")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 分页查询岗位
     * @param page 分页对象
     * @param post 岗位
     * @return R
     * @real_return R<Page<Post>>
     */
    @Operation(description = "分页查询岗位")
    @GetMapping("/posts")
    @PreAuthorize("@pms.hasPermission('ims:post:admin')")
    public R<IPage<Post>> handlePosts(Page<Post> page, Post post) {
        return R.ok(postService.findPosts(page, post));
    }

    /**
     * 查询岗位
     * @return R<List<Post>>
     */
    @Operation(description = "查询岗位")
    @GetMapping("/post")
    @PreAuthorize("@pms.hasPermission('ims:post:admin')")
    public R<List<Post>> handleAllPost() {
        return R.ok(postService.list());
    }

    /**
     * 通过id查询岗位
     * @param id id
     * @return R
     */
    @Operation(description = "通过id查询岗位")
    @GetMapping("/post/{id}")
    @PreAuthorize("@pms.hasPermission('ims:post:admin')")
    public R<Post> handleId(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(postService.findById(id));
    }

    /**
     * 添加或编辑岗位
     * @param post 岗位
     * @return R
     */
    @Operation(description = "添加或编辑岗位")
    @PostMapping("/post")
    @PreAuthorize("@pms.hasPermission('ims:post:add') or @pms.hasPermission('ims:post:edit')")
    public R<Post> handleSaveOrUpdate(@Valid @Parameter(name = "岗位" , required = true) @RequestBody Post post) {
        return R.ok(postService.saveOrUpdatePost(post));
    }

    /**
     * 通过id删除岗位
     * @param ids ID串
     * @return R
     */
    @Operation(description = "通过id删除岗位")
    @DeleteMapping("/post")
    @PreAuthorize("@pms.hasPermission('ims:post:delete')")
    public R<Boolean> handleDelete(@Parameter(name = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(postService.deleteByIds(ids));
    }

    /**
     * 更新岗位状态
     *
     * @param post 岗位
     * @return R
     */
    @Operation(description = "更新岗位状态")
    @PostMapping("/post/status")
    @PreAuthorize("@pms.hasPermission('ims:post:admin')")
    public R<Boolean> handleUpdateStatus(@Parameter(name = "岗位" , required = true) @RequestBody Post post) {
        return R.ok(postService.updateStatus(post));
    }

    /**
     * 根据用户ID查询岗位
     * 返回岗位集合
     *
     * @param userId 用户id
     * @return R<List<Post>>
     */
    @Operation(description = "根据用户ID查询岗位")
    @GetMapping(value = "/post", params = "userId")
    @PreAuthorize("@pms.hasPermission('ims:post:admin')")
    public R<List<Post>> handlePostsByUserId(@Parameter(name = "用户ID", required = true) @RequestParam("userId") @Min(1) Long userId) {
        return R.ok(postService.findPostsByUserId(userId));
    }

}
