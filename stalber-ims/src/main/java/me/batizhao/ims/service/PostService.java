package me.batizhao.ims.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.ims.domain.Post;

import java.util.List;

/**
 * 岗位接口类
 *
 * @author batizhao
 * @since 2021-04-22
 */
public interface PostService extends IService<Post> {

    /**
     * 分页查询岗位
     * @param page 分页对象
     * @param post 岗位
     * @return IPage<Post>
     */
    IPage<Post> findPosts(Page<Post> page, Post post);

    /**
     * 通过id查询岗位
     * @param id id
     * @return Post
     */
    Post findById(Long id);

    /**
     * 添加或编辑岗位
     * @param post 岗位
     * @return Post
     */
    Post saveOrUpdatePost(Post post);

    /**
     * 删除
     * @param ids
     * @return
     */
    Boolean deleteByIds(List<Long> ids);

    /**
     * 更新岗位状态
     * @param post 岗位
     * @return Boolean
     */
    Boolean updateStatus(Post post);

    /**
     * 通过用户 ID 查相关的岗位
     * @param userId
     * @return
     */
    List<Post> findPostsByUserId(Long userId);
}
