package me.batizhao.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.app.domain.AppProcess;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p> 应用流程 </p>
 *
 * @author wws
 * @since 2022-02-28 14:58
 */
@Mapper
public interface AppProcessMapper extends BaseMapper<AppProcess> {
}
