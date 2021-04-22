package me.batizhao.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.ims.domain.UserPost;

import java.util.List;

/**
 * 用户岗位关联接口类
 *
 * @author batizhao
 * @since 2021-04-22
 */
public interface UserPostService extends IService<UserPost> {

    /**
     * 更新用户岗位
     * @param userPosts
     * @return
     */
    Boolean updateUserPosts(List<UserPost> userPosts);
}
