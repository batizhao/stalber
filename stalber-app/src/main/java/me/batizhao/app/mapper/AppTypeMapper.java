package me.batizhao.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.app.domain.AppType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应用分类
 *
 * @author batizhao
 * @since 2022-01-21
 */
@Mapper
public interface AppTypeMapper extends BaseMapper<AppType> {

}
