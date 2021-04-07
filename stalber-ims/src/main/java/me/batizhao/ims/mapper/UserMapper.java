package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.ims.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
