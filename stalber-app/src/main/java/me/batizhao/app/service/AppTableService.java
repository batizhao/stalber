package me.batizhao.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.app.domain.AppTable;
import me.batizhao.app.domain.AppTableColumn;

import java.util.List;
import java.util.Map;

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
     * @param appTable 应用表元数据
     * @return AppTable
     */
    AppTable saveOrUpdateAppTable(AppTable appTable);

    /**
     * 更新 codeMetadata
     * @param appTable 应用表元数据
     * @return AppTable
     */
    Boolean updateCodeMetadataById(AppTable appTable);

    /**
     * 同步表到数据库
     * @param id appTable.id
     * @return
     */
    Boolean syncTable(Long id);

    /**
     * 生成代码 zip
     * @param id
     * @return byte[]
     */
    byte[] downloadCode(Long id);

    /**
     * 生成代码 path
     * @param id
     * @return
     */
    Boolean generateCode(Long id);

    /**
     * 预览代码
     * @param id
     * @return
     */
    Map<String, String> previewCode(Long id);

    /**
     * 查询数据源下的所有表
     * @param page 分页对象
     * @param dsName 数据源
     * @return IPage<AppTable>
     */
    IPage<AppTable> findTables(Page<AppTable> page, AppTable appTable, String dsName);

    /**
     * 导入选中的表
     * @param appTables
     * @return
     */
    Boolean importTables(List<AppTable> appTables);

    /**
     * 查询表原始信息
     * @param tableName 表名
     * @param dsName 动态数据源名
     * @return
     */
    List<AppTableColumn> findColumnsByTableName(String tableName, String dsName);

    /**
     * 查询可以关联的表清单
     * @param id
     * @return
     */
    List<AppTable> listTableRelations(Long id);
}
