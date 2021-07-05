package me.batizhao.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.oa.domain.Comment;
import me.batizhao.oa.domain.CommentAndTask;
import me.batizhao.oa.domain.Task;

import java.util.List;

/**
 * 审批接口类
 *
 * @author batizhao
 * @since 2021-06-10
 */
public interface CommentService extends IService<Comment> {

    /**
     * 分页查询审批
     * @param page 分页对象
     * @param comment 审批
     * @return IPage<Comment>
     */
    IPage<Comment> findComments(Page<Comment> page, Comment comment);

    /**
     * 查询审批
     * @param comment
     * @return List<Comment>
     */
    List<Comment> findComments(Comment comment);

    /**
     * 通过id查询审批
     * @param id id
     * @return Comment
     */
    Comment findById(Long id);

    /**
     * 添加或编辑审批
     * @param comment 审批
     * @return Comment
     */
    Comment saveOrUpdateComment(CommentAndTask cat);

    /**
     * 更新审批状态
     * @param comment 审批
     * @return Boolean
     */
    Boolean updateStatus(Comment comment);
}
