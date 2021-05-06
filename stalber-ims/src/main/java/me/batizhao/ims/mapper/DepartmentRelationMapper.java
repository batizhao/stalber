package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.ims.domain.DepartmentRelation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门依赖关系
 *
 * @author batizhao
 * @since 2021-04-29
 */
@Mapper
public interface DepartmentRelationMapper extends BaseMapper<DepartmentRelation> {

}