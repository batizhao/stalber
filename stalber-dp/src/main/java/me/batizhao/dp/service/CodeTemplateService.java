package me.batizhao.dp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.common.core.domain.FolderTree;
import me.batizhao.dp.domain.CodeTemplate;
import me.batizhao.dp.domain.CodeTemplateDTO;

import java.util.List;

/**
 * 模板配置接口类
 *
 * @author batizhao
 * @since 2021-10-12
 */
public interface CodeTemplateService extends IService<CodeTemplate> {

    /**
     * 查询模板配置
     * @return List<FolderTree>
     */
    List<FolderTree> findCodeTemplateTree();

    /**
     * 通过id查询模板配置
     * @param path path
     * @return String
     */
    String findByPath(String path);

    /**
     * 添加或编辑模板配置
     * @param codeTemplateDTO 代码
     * @return boolean
     */
    boolean saveOrUpdateCodeTemplate(CodeTemplateDTO codeTemplateDTO);

}
