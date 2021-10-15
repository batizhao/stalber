package me.batizhao.dp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.exception.NotFoundException;
import me.batizhao.dp.domain.CodeTemplate;
import me.batizhao.dp.mapper.CodeTemplateMapper;
import me.batizhao.dp.service.CodeTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 模板配置接口实现类
 *
 * @author batizhao
 * @since 2021-10-12
 */
@Service
public class CodeTemplateServiceImpl extends ServiceImpl<CodeTemplateMapper, CodeTemplate> implements CodeTemplateService {

    @Autowired
    private CodeTemplateMapper codeTemplateMapper;

    @Override
    public IPage<CodeTemplate> findCodeTemplates(Page<CodeTemplate> page, CodeTemplate codeTemplate) {
        LambdaQueryWrapper<CodeTemplate> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(codeTemplate.getProjectKey())) {
            wrapper.like(CodeTemplate::getProjectKey, codeTemplate.getProjectKey());
        }
        if (StringUtils.isNotBlank(codeTemplate.getName())) {
            wrapper.like(CodeTemplate::getName, codeTemplate.getName());
        }
        return codeTemplateMapper.selectPage(page, wrapper);
    }

    @Override
    public List<CodeTemplate> findCodeTemplates(CodeTemplate codeTemplate) {
        LambdaQueryWrapper<CodeTemplate> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(codeTemplate.getProjectKey())) {
            wrapper.like(CodeTemplate::getProjectKey, codeTemplate.getProjectKey());
        }
        if (StringUtils.isNotBlank(codeTemplate.getName())) {
            wrapper.like(CodeTemplate::getName, codeTemplate.getName());
        }
        return codeTemplateMapper.selectList(wrapper);
    }

    @Override
    public List<CodeTemplate> findCodeTemplates(String projectKey) {
        LambdaQueryWrapper<CodeTemplate> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(projectKey)) {
            wrapper.eq(CodeTemplate::getProjectKey, projectKey);
        }
        wrapper.eq(CodeTemplate::getStatus, "open");
        return codeTemplateMapper.selectList(wrapper);
    }

    @Override
    public CodeTemplate findById(Long id) {
        CodeTemplate codeTemplate = codeTemplateMapper.selectById(id);

        if(codeTemplate == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return codeTemplate;
    }

    @Override
    @Transactional
    public CodeTemplate saveOrUpdateCodeTemplate(CodeTemplate codeTemplate) {
        if (codeTemplate.getId() == null) {
            codeTemplate.setCreateTime(LocalDateTime.now());
            codeTemplate.setUpdateTime(LocalDateTime.now());
            codeTemplateMapper.insert(codeTemplate);
        } else {
            codeTemplate.setUpdateTime(LocalDateTime.now());
            codeTemplateMapper.updateById(codeTemplate);
        }

        return codeTemplate;
    }


    @Override
    @Transactional
    public Boolean updateStatus(CodeTemplate codeTemplate) {
        LambdaUpdateWrapper<CodeTemplate> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(CodeTemplate::getId, codeTemplate.getId()).set(CodeTemplate::getStatus, codeTemplate.getStatus());
        return codeTemplateMapper.update(null, wrapper) == 1;
    }

}
