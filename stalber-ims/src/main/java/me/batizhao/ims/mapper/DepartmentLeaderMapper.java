package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.ims.domain.DepartmentLeader;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门领导关联
 *
 * @author batizhao
 * @since 2021-04-26
 */
@Mapper
public interface DepartmentLeaderMapper extends BaseMapper<DepartmentLeader> {

}