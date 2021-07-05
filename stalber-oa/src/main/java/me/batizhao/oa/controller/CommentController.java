package me.batizhao.oa.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.util.R;
import me.batizhao.oa.domain.Comment;
import me.batizhao.oa.domain.CommentAndTask;
import me.batizhao.oa.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 审批 API
 *
 * @module oa
 *
 * @author batizhao
 * @since 2021-06-10
 */
@Api(tags = "审批管理")
@RestController
@Slf4j
@Validated
@RequestMapping("oa")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 分页查询审批
     * @param page 分页对象
     * @param comment 审批
     * @return R
     * @real_return R<Page<Comment>>
     */
    @ApiOperation(value = "分页查询审批")
    @GetMapping("/comments")
    @PreAuthorize("@pms.hasPermission('oa:comment:admin')")
    public R<IPage<Comment>> handleComments(Page<Comment> page, Comment comment) {
        return R.ok(commentService.findComments(page, comment));
    }

    /**
     * 查询审批
     * @return R<List<Comment>>
     */
    @ApiOperation(value = "查询审批")
    @GetMapping("/comment")
    @PreAuthorize("@pms.hasPermission('oa:comment:admin')")
    public R<List<Comment>> handleComments(Comment comment) {
        return R.ok(commentService.findComments(comment));
    }

    /**
     * 通过id查询审批
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询审批")
    @GetMapping("/comment/{id}")
    @PreAuthorize("@pms.hasPermission('oa:comment:admin')")
    public R<Comment> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(commentService.findById(id));
    }

    /**
     * 添加或编辑审批
     * @param cat 审批
     * @return R
     */
    @ApiOperation(value = "添加或编辑审批")
    @PostMapping("/comment")
//    @PreAuthorize("@pms.hasPermission('oa:comment:add') or @pms.hasPermission('oa:comment:edit')")
    public R<Comment> handleSaveOrUpdate(@Valid @ApiParam(value = "审批" , required = true) @RequestBody CommentAndTask cat) {
        return R.ok(commentService.saveOrUpdateComment(cat));
    }

    /**
     * 通过id删除审批
     * @param ids ID串
     * @return R
     */
    @ApiOperation(value = "通过id删除审批")
    @DeleteMapping("/comment")
    @PreAuthorize("@pms.hasPermission('oa:comment:delete')")
    public R<Boolean> handleDelete(@ApiParam(value = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(commentService.removeByIds(ids));
    }

    /**
     * 更新审批状态
     *
     * @param comment 审批
     * @return R
     */
    @ApiOperation(value = "更新审批状态")
    @PostMapping("/comment/status")
    @PreAuthorize("@pms.hasPermission('oa:comment:admin')")
    public R<Boolean> handleUpdateStatus(@ApiParam(value = "审批" , required = true) @RequestBody Comment comment) {
        return R.ok(commentService.updateStatus(comment));
    }

}
