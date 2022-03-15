package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.ims.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    List<User> selectLeadersByDepartmentId(@Param("id") Integer id, @Param("type") String type);

    /**
     * 查询用户
     * @param page
     * @param user
     * @return
     */
    IPage<User> selectUsers(Page<User> page, @Param("user") User user, @Param("departmentId") Long departmentId);

    /**
     * 查角色用户
     * @param roleId
     * @return
     */
    @Select("SELECT A.id, A.name, A.username FROM user A LEFT JOIN user_role B ON A.id = B.userId WHERE B.roleId = #{roleId}")
    List<User> findUsersByRoleId(@Param("roleId") Long roleId);
}
