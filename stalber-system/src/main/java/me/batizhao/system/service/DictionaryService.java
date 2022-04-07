package me.batizhao.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.system.domain.Dictionary;
import me.batizhao.system.domain.DictionaryData;

import java.util.List;

/**
 * 字典类型接口类
 *
 * @author batizhao
 * @since 2021-02-07
 */
public interface DictionaryService extends IService<Dictionary> {

    /**
     * 分页查询
     * @param page 分页对象
     * @param dictionary 字典类型
     * @return IPage<DictType>
     */
    IPage<Dictionary> findDictTypes(Page<Dictionary> page, Dictionary dictionary);

    /**
     * 根据标识查询字典
     * @param code
     * @return
     */
    List<DictionaryData> findByCode(String code);

    /**
     * 通过id查询字典类型
     * @param id id
     * @return DictType
     */
    Dictionary findById(Long id);

    /**
     * 添加或修改字典类型
     * @param dictionary 字典类型
     * @return DictType
     */
    Dictionary saveOrUpdateDictType(Dictionary dictionary);

    /**
     * 更新字典类型状态
     * @param dictionary 字典类型
     * @return Boolean
     */
    Boolean updateStatus(Dictionary dictionary);

}
