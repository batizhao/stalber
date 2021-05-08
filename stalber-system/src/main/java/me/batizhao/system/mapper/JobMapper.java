package me.batizhao.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.system.domain.SysJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务调度
 *
 * @author batizhao
 * @since 2021-05-07
 */
@Mapper
public interface JobMapper extends BaseMapper<SysJob> {

}
