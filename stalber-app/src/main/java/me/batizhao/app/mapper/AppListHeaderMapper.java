package me.batizhao.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.app.domain.AppListHeader;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p> 应用列表表头 </p>
 *
 * @author wws
 * @since 2022-03-02 19:55
 */
@Mapper
public interface AppListHeaderMapper extends BaseMapper<AppListHeader> {
}
