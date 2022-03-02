package me.batizhao.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.app.domain.AppList;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p> 应用列表 </p>
 *
 * @author wws
 * @since 2022-03-02 19:55
 */
@Mapper
public interface AppListMapper extends BaseMapper<AppList> {
}
