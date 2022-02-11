package me.batizhao.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.app.domain.AppTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 应用表
 *
 * @author batizhao
 * @since 2022-01-27
 */
@Mapper
public interface AppTableMapper extends BaseMapper<AppTable> {

}
