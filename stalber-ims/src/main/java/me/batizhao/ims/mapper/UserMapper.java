package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.ims.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据部门ID查询领导
     * @param id
     * @param type
     * @return
     */
    List<User> selectLeadersByDepartmentId(@Param("id") Long id, @Param("type") String type);

    /**
     * 查询用户
     * @param page
     * @param user
     * @return
     */
    IPage<User> selectUsers(Page<User> page, @Param("user") User user, @Param("departmentId") Long departmentId);
}
