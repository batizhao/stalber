package me.batizhao.dp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.common.domain.FolderTree;
import me.batizhao.dp.domain.CodeTemplate;

import java.util.List;

/**
 * 模板配置接口类
 *
 * @author batizhao
 * @since 2021-10-12
 */
public interface CodeTemplateService extends IService<CodeTemplate> {

    /**
     * 分页查询模板配置
     * @param codeTemplate 模板配置
     * @return IPage<CodeTemplate>
     */
    List<FolderTree> findCodeTemplateTree(CodeTemplate codeTemplate);

//    /**
//     * 查询模板配置
//     * @param codeTemplate
//     * @return List<CodeTemplate>
//     */
//    List<CodeTemplate> findCodeTemplates(CodeTemplate codeTemplate);

    /**
     * 通过 projectKey 查询模板配置
     * @param projectKey
     * @return List<CodeTemplate>
     */
    List<CodeTemplate> findCodeTemplates(String projectKey);

    /**
     * 通过id查询模板配置
     * @param path path
     * @return String
     */
    String findByPath(String path);

    /**
     * 添加或编辑模板配置
     * @param codeTemplate 模板配置
     * @return CodeTemplate
     */
    CodeTemplate saveOrUpdateCodeTemplate(CodeTemplate codeTemplate);

    /**
     * 更新模板配置状态
     * @param codeTemplate 模板配置
     * @return Boolean
     */
    Boolean updateStatus(CodeTemplate codeTemplate);

}
