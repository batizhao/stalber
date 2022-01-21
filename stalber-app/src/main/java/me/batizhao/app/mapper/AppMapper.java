package me.batizhao.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.app.domain.App;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应用
 *
 * @author batizhao
 * @since 2022-01-21
 */
@Mapper
public interface AppMapper extends BaseMapper<App> {

}
