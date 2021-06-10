package me.batizhao.oa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.oa.domain.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审批
 *
 * @author batizhao
 * @since 2021-06-10
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
