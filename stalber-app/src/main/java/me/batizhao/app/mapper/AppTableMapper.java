package me.batizhao.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.app.domain.AppTable;
import me.batizhao.dp.domain.Code;
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

    /**
     * 根据数据源查询表
     * @param page
     * @return
     */
    IPage<AppTable> selectTablePageByDs(Page<AppTable> page, @Param("appTable") AppTable appTable);

}
