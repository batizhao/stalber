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
     * 查询所有部门
     * @param department
     * @return
     */
    List<Department> selectDepartments(@Param("department") Department department);

    /**
     * 查用户部门
     * @param id
     * @return
     */
    @Select("SELECT A.id, A.name FROM department A LEFT JOIN user_department B ON A.id = B.departmentId WHERE B.userId = #{id}")
    List<Department> findDepartmentsByUserId(@Param("id") Long id);

    /**
     * 查角色部门
     * @param id
     * @return
     */
    @Select("SELECT A.id, A.name FROM department A LEFT JOIN role_department B ON A.id = B.departmentId WHERE B.roleId = #{id}")
    List<Department> findDepartmentsByRoleId(@Param("id") Long id);

}
