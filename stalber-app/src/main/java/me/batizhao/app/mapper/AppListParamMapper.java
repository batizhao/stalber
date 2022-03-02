package me.batizhao.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.app.domain.AppListParam;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p> 应用列表按钮参数 </p>
 *
 * @author wws
 * @since 2022-03-02 19:56
 */
@Mapper
public interface AppListParamMapper extends BaseMapper<AppListParam> {
}
