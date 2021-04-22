package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.ims.domain.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 岗位
 *
 * @author batizhao
 * @since 2021-04-22
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {

    /**
     * 查用户角色
     * @param id
     * @return
     */
    @Select("SELECT A.id, A.name, A.code FROM post A LEFT JOIN user_post B ON A.id = B.postId WHERE B.userId = #{id}")
    List<Post> findPostsByUserId(@Param("id") Long id);

}
