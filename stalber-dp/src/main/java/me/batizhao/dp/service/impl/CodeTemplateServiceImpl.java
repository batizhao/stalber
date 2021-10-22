package me.batizhao.dp.service.impl;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.config.FileProperties;
import me.batizhao.common.domain.FolderTree;
import me.batizhao.common.util.FolderUtil;
import me.batizhao.dp.domain.CodeTemplate;
import me.batizhao.dp.domain.CodeTemplateDTO;
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
    @Autowired
    private FileProperties fileProperties;

    @Override
    public List<FolderTree> findCodeTemplateTree() {
        List<FolderTree> folderTree = FolderUtil.build(new FolderTree(fileProperties.getCodeTemplateLocation()));
        return FolderUtil.build(folderTree);
    }

    @Override
    public String findByPath(String path) {
        FileReader fileReader = new FileReader(path);
        return fileReader.readString();
    }

    @Override
    public boolean saveOrUpdateCodeTemplate(CodeTemplateDTO codeTemplateDTO) {
        FileWriter writer = new FileWriter(codeTemplateDTO.getPath());
        return writer.write(codeTemplateDTO.getCodeContent()).exists();
    }
}
