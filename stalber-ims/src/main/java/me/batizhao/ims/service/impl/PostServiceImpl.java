package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.ims.domain.Post;
import me.batizhao.ims.domain.UserPost;
import me.batizhao.ims.mapper.PostMapper;
import me.batizhao.ims.service.PostService;
import me.batizhao.ims.service.UserPostService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 岗位接口实现类
 *
 * @author batizhao
 * @since 2021-04-22
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserPostService userPostService;

    @Override
    public IPage<Post> findPosts(Page<Post> page, Post post) {
        LambdaQueryWrapper<Post> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(post.getName())) {
            wrapper.like(Post::getName, post.getName());
        }
        return postMapper.selectPage(page, wrapper);
    }

    @Override
    public Post findById(Long id) {
        Post post = postMapper.selectById(id);

        if(post == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return post;
    }

    @Override
    @Transactional
    public Post saveOrUpdatePost(Post post) {
        if (post.getId() == null) {
            post.setCreateTime(LocalDateTime.now());
            post.setUpdateTime(LocalDateTime.now());
            postMapper.insert(post);
        } else {
            post.setUpdateTime(LocalDateTime.now());
            postMapper.updateById(post);
        }

        return post;
    }

    @Override
    @Transactional
    public Boolean deleteByIds(List<Long> ids) {
        this.removeByIds(ids);
        ids.forEach(i -> {
            userPostService.remove(Wrappers.<UserPost>lambdaQuery().eq(UserPost::getPostId, i));
        });
        return true;
    }

    @Override
    @Transactional
    public Boolean updateStatus(Post post) {
        LambdaUpdateWrapper<Post> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Post::getId, post.getId()).set(Post::getStatus, post.getStatus());
        return postMapper.update(null, wrapper) == 1;
    }

    @Override
    public List<Post> findPostsByUserId(Long userId) {
        return postMapper.findPostsByUserId(userId);
    }
}
