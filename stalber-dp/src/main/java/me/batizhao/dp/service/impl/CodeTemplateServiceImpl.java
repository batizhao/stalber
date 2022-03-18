package me.batizhao.dp.service.impl;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.config.CodeProperties;
import me.batizhao.common.core.domain.FolderTree;
import me.batizhao.common.core.util.FolderUtil;
import me.batizhao.dp.domain.CodeTemplateDTO;
import me.batizhao.dp.service.CodeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.List;

/**
 * 模板配置接口实现类
 *
 * @author batizhao
 * @since 2021-10-12
 */
@Service
@Slf4j
public class CodeTemplateServiceImpl implements CodeTemplateService {

    @Autowired
    private CodeProperties codeProperties;

    @Override
    public List<FolderTree> findCodeTemplateTree() {
        String templatePath = Paths.get(".", codeProperties.getTemplateUrl()).toAbsolutePath().toString();
        log.info("####### templatePath: {}", templatePath);
        List<FolderTree> folderTree = FolderUtil.build(new FolderTree(templatePath));
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
