package me.batizhao.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.app.domain.App;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 应用
 *
 * @author batizhao
 * @since 2022-01-21
 */
@Mapper
public interface AppMapper extends BaseMapper<App> {

    /**
     * 创建数据库表
     *
     * @param script 表脚本
     */
    int createTable(@Param("script") String script);

}
