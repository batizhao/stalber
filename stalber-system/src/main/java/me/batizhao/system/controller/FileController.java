package me.batizhao.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.util.R;
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
@Api(tags = "文件管理")
@RestController
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 插入文件
     *
     * @param file 文件实体
     * @return File
     */
    @ApiOperation(value = "插入文件")
    @PostMapping("/system/file/upload")
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
    @ApiOperation(value = "根据文件名显示图片")
    @GetMapping("/system/file/image/{name:^.+\\.(?:jpeg|jpg|png|JPEG|JPG|PNG)$}")
    public ResponseEntity<Resource> handleImageByName(@ApiParam(value = "图片名", required = true) @PathVariable("name") String name) {
        Resource resource = fileService.loadAsResource(name);

        java.io.File file = new java.io.File(name);
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        log.info("resource: {}, file: {}, mimeType: {}", resource, file, mimeType);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
