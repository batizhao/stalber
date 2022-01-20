package me.batizhao.system.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.R;
import me.batizhao.system.domain.File;
import me.batizhao.system.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLConnection;

/**
 * 文件管理
 * 上传、下载
 *
 * @module pecado-system
 *
 * @author batizhao
 * @date 2020/9/23
 */
@Tag(name = "文件管理")
@RestController
@Slf4j
@RequestMapping("system")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 插入文件
     *
     * @param file 文件实体
     * @return File
     */
    @Operation(description = "插入文件")
    @PostMapping("/file/upload")
    public R<File> handleSave(@RequestParam("file") MultipartFile file) {
        return R.ok(fileService.upload(file));
    }

    /**
     * 根据文件名显示图片
     * 返回图片
     *
     * @param name 文件名
     * @return 返回图片详情
     */
    @SneakyThrows
    @Operation(description = "根据文件名显示图片")
    @GetMapping("/file/image/{name:^.+\\.(?:jpeg|jpg|png|JPEG|JPG|PNG)$}")
    public ResponseEntity<Resource> handleImageByName(@Parameter(name = "图片名", required = true) @PathVariable("name") String name) {
        Resource resource = fileService.load(name);

        java.io.File file = new java.io.File(name);
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        log.info("resource: {}, file: {}, mimeType: {}", resource, file, mimeType);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
