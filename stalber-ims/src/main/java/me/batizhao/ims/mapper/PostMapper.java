package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.ims.domain.Post;
import org.apache.ibatis.annotations.Mapper;

/**
 * 岗位
 *
 * @author batizhao
 * @since 2021-04-22
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {

}
