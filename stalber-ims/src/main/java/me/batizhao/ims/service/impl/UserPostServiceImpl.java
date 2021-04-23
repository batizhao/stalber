package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.ims.domain.UserPost;
import me.batizhao.ims.mapper.UserPostMapper;
import me.batizhao.ims.service.UserPostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户岗位关联接口实现类
 *
 * @author batizhao
 * @since 2021-04-22
 */
@Service
public class UserPostServiceImpl extends ServiceImpl<UserPostMapper, UserPost> implements UserPostService {

    @Override
    @Transactional
    public Boolean updateUserPosts(List<UserPost> userPosts) {
        this.remove(Wrappers.<UserPost>lambdaQuery().eq(UserPost::getUserId, userPosts.get(0).getUserId()));
        return saveBatch(userPosts);
    }
}
