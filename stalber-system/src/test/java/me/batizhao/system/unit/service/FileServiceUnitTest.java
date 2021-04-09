package me.batizhao.system.unit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.exception.StorageException;
import me.batizhao.common.util.FileNameAndPathUtils;
import me.batizhao.system.config.FileUploadProperties;
import me.batizhao.system.domain.File;
import me.batizhao.system.mapper.FileMapper;
import me.batizhao.system.service.FileService;
import me.batizhao.system.service.impl.FileServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author batizhao
 * @date 2020/9/25
 */
@Slf4j
public class FileServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    @EnableConfigurationProperties(value = FileUploadProperties.class)
    static class TestContextConfiguration {
        @Autowired
        FileUploadProperties fileUploadProperties;

        @Bean
        public FileService fileService() {
            return new FileServiceImpl(fileUploadProperties);
        }
    }

    @MockBean
    private FileMapper fileMapper;

    @Autowired
    FileService fileService;
    @SpyBean
    private IService service;

    @Test
    public void givenFile_whenUpload_thenSuccess() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "test data".getBytes());

        try (MockedStatic<FileNameAndPathUtils> mockStatic = mockStatic(FileNameAndPathUtils.class)) {
            mockStatic.when(() -> {
                FileNameAndPathUtils.fileNameEncode(anyString());
            }).thenReturn("56d4728df6cc291449b01f5053bddbad.txt");

            mockStatic.when(() -> {
                FileNameAndPathUtils.pathEncode(anyString());
            }).thenReturn("39/ef/56d4728df6cc291449b01f5053bddbad.txt");

            File result = fileService.upload(mockMultipartFile);

            log.info("result: {}", result);

            assertThat(result, hasProperty("name", equalTo("test.txt")));
        }
    }

    @Test
    public void givenNothing_whenUpload_thenException() {
        Exception exception = assertThrows(StorageException.class, () -> fileService.upload(null));
        assertThat(exception.getMessage(), containsString("Failed to store null file"));

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "..test.txt",
                "text/plain", "test data".getBytes());
        exception = assertThrows(StorageException.class, () -> fileService.upload(mockMultipartFile));
        assertThat(exception.getMessage(), containsString("Cannot store file with relative path outside current directory"));

        MockMultipartFile mockMultipartFile2 = new MockMultipartFile("file", "test2.txt",
                "text/plain", "".getBytes());
        exception = assertThrows(StorageException.class, () -> fileService.upload(mockMultipartFile2));
        assertThat(exception.getMessage(), containsString("Failed to store empty file"));
    }

    @Test
    public void givenFileName_whenLoadResource_thenSuccess() throws IOException {
        try (MockedStatic<FileNameAndPathUtils> mockStatic = mockStatic(FileNameAndPathUtils.class)) {
            mockStatic.when(() -> {
                FileNameAndPathUtils.pathEncode(anyString());
            }).thenReturn("39/ef/56d4728df6cc291449b01f5053bddbad.txt");

            Resource resource = fileService.loadAsResource("xxx");

            log.info("resource: {}", resource);

            assertThat(resource.getURL().toString(), equalTo("file:/tmp/39/ef/56d4728df6cc291449b01f5053bddbad.txt"));
        }
    }

    @Test
    public void givenFileName_whenLoadResource_thenNotFound() {
        try (MockedStatic<FileNameAndPathUtils> mockStatic = mockStatic(FileNameAndPathUtils.class)) {
            mockStatic.when(() -> {
                FileNameAndPathUtils.pathEncode(any(String.class));
            }).thenReturn("xxx.txt");

            assertThrows(StorageException.class, () -> fileService.loadAsResource("xxx"));
        }
    }

    @Test
    public void givenFile_whenSaveAndUpload_thenSuccess() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test2.txt",
                "text/plain", "test data2".getBytes());

        File file = new File().setFileName("hexFileName").setName("filename")
                .setSize(100L).setUrl("xxx/test2.txt")
                .setCreateTime(LocalDateTime.now());

        doReturn(file).when(fileService).upload(any(MockMultipartFile.class));
        doReturn(true).when(service).save(any(File.class));

        File result = fileService.uploadAndSave(mockMultipartFile);

        log.info("result: {}", result);

        assertThat(result, hasProperty("name", equalTo("filename")));
    }
}
