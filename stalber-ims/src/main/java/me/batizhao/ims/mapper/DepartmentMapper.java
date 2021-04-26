package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.ims.domain.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 部门
 *
 * @author batizhao
 * @since 2021-04-25
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 查用户角色
     * @param id
     * @return
     */
    @Select("SELECT A.id, A.name FROM department A LEFT JOIN user_department B ON A.id = B.departmentId WHERE B.userId = #{id}")
    List<Department> findDepartmentsByUserId(@Param("id") Long id);
}
