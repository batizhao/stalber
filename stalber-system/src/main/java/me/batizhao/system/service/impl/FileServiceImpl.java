package me.batizhao.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.exception.StorageException;
import me.batizhao.common.core.util.FileNameAndPathUtils;
import me.batizhao.oss.api.StorageService;
import me.batizhao.system.domain.File;
import me.batizhao.system.mapper.FileMapper;
import me.batizhao.system.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * @author batizhao
 * @date 2020/9/23
 */
@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Autowired
    private StorageService storageService;

    @Override
    @SneakyThrows
    public File upload(MultipartFile file) {
        if (file == null) {
            throw new StorageException("Failed to store null file.");
        }

        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file " + filename);
        }
        if (filename.contains("..")) {
            // This is a security check
            throw new StorageException("Cannot store file with relative path outside current directory " + filename);
        }

        String hexFileName = FileNameAndPathUtils.fileNameEncode(filename);
        String targetPath = FileNameAndPathUtils.pathEncode(hexFileName);
        storageService.upload(Paths.get(targetPath), file.getInputStream());

        //只返回文件名给前端，不包括路径
        return new File().setFileName(hexFileName).setName(filename)
                .setSize(file.getSize()).setUrl(targetPath)
                .setCreateTime(LocalDateTime.now());
    }

    @Override
    @Transactional
    public File uploadAndSave(MultipartFile file) {
        File f = upload(file);
        save(f);
        return f;
    }

    @Override
    @SneakyThrows
    public Resource load(String filename) {
        Path path = Paths.get(FileNameAndPathUtils.pathEncode(filename));
        return new InputStreamResource(storageService.get(path));
    }

}
