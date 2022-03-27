package me.batizhao.app.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.app.domain.AppTable;
import me.batizhao.app.domain.AppTableColumn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 查询表列信息
     * @param tableName 表名称
     * @return
     */
    @DS("#last")
    List<AppTableColumn> selectColumnsByTableName(@Param("tableName") String tableName, String dsName);

}
