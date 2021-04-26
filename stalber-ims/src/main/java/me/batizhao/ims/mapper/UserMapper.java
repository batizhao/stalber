package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
     * @return
     */
    @Select("SELECT A.id, A.name, A.username FROM user A LEFT JOIN department_leader B ON A.id = B.leaderUserId WHERE B.departmentId = #{id}")
    List<User> selectLeadersByDepartmentId(@Param("id") Long id);

}
