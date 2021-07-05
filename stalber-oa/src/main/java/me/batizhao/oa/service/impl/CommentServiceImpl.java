package me.batizhao.oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.exception.NotFoundException;
import me.batizhao.oa.domain.Comment;
import me.batizhao.oa.domain.CommentAndTask;
import me.batizhao.oa.domain.Task;
import me.batizhao.oa.mapper.CommentMapper;
import me.batizhao.oa.service.CommentService;
import me.batizhao.oa.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 审批接口实现类
 *
 * @author batizhao
 * @since 2021-06-10
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private TaskService taskService;

    @Override
    public IPage<Comment> findComments(Page<Comment> page, Comment comment) {
        LambdaQueryWrapper<Comment> wrapper = Wrappers.lambdaQuery();
        return commentMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Comment> findComments(Comment comment) {
        LambdaQueryWrapper<Comment> wrapper = Wrappers.lambdaQuery();
        return commentMapper.selectList(wrapper);
    }

    @Override
    public Comment findById(Long id) {
        Comment comment = commentMapper.selectById(id);

        if(comment == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return comment;
    }

    @Override
    @Transactional
    public Comment saveOrUpdateComment(CommentAndTask cat) {
        if (cat.getComment().getId() == null) {
            cat.getComment().setCreateTime(LocalDateTime.now());
            cat.getComment().setUpdateTime(LocalDateTime.now());
            commentMapper.insert(cat.getComment());

            taskService.start(cat.getTask());
        } else {
            cat.getComment().setUpdateTime(LocalDateTime.now());
            commentMapper.updateById(cat.getComment());
        }

        return cat.getComment();
    }

    @Override
    @Transactional
    public Boolean updateStatus(Comment comment) {
        LambdaUpdateWrapper<Comment> wrapper = Wrappers.lambdaUpdate();
//        wrapper.eq(Comment::getId, comment.getId()).set(Comment::getStatus, comment.getStatus());
        return commentMapper.update(null, wrapper) == 1;
    }
}
