package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.ims.domain.UserDepartment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户部门关联
 *
 * @author batizhao
 * @since 2021-04-26
 */
@Mapper
public interface UserDepartmentMapper extends BaseMapper<UserDepartment> {

}