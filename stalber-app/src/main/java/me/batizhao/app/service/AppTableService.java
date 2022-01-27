package me.batizhao.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.app.domain.AppTable;

import java.util.List;

/**
 * 应用表接口类
 *
 * @author batizhao
 * @since 2022-01-27
 */
public interface AppTableService extends IService<AppTable> {

    /**
     * 分页查询应用表
     * @param page 分页对象
     * @param appTable 应用表
     * @return IPage<AppTable>
     */
    IPage<AppTable> findAppTables(Page<AppTable> page, AppTable appTable);

    /**
     * 查询应用表
     * @param appTable
     * @return List<AppTable>
     */
    List<AppTable> findAppTables(AppTable appTable);

    /**
     * 通过id查询应用表
     * @param id id
     * @return AppTable
     */
    AppTable findById(Long id);

    /**
     * 添加或编辑应用表
     * @param appTables 应用表字段集合
     * @return AppTable
     */
    Boolean saveOrUpdateAppTable(List<AppTable> appTables);


}
