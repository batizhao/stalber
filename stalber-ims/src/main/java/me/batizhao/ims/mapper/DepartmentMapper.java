package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.ims.domain.Department;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门
 *
 * @author batizhao
 * @since 2021-04-25
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

}
