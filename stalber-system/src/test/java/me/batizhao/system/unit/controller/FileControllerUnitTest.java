package me.batizhao.system.unit.controller;

import me.batizhao.common.constant.ResultEnum;
import me.batizhao.system.controller.FileController;
import me.batizhao.system.domain.File;
import me.batizhao.system.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @date 2020/9/28
 */
@WebMvcTest(FileController.class)
public class FileControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FileService fileService;

    @Test
    @WithMockUser
    public void givenFile_whenUpload_thenSuccess() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.txt",
                MediaType.TEXT_PLAIN_VALUE, "test data".getBytes());

        File file = new File().setFileName("hexFileName").setName("filename")
                .setSize(100L).setUrl("xxx/test2.txt")
                .setCreateTime(LocalDateTime.now());

        when(fileService.upload(any(MultipartFile.class))).thenReturn(file);

        mvc.perform(multipart("/system/file/upload").file(mockMultipartFile).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.name", equalTo("filename")));

        verify(fileService).upload(any(MultipartFile.class));
    }

    @Test
    @WithMockUser
    public void givenImageFileName_whenLoadResource_thenSuccess() throws Exception {
        Resource file = new ClassPathResource("test.jpg");
        when(fileService.loadAsResource(anyString())).thenReturn(file);

        mvc.perform(get("/system/file/image/test.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG_VALUE));

        verify(fileService).loadAsResource(anyString());
    }

    @Test
    @WithMockUser
    public void givenPngFileName_whenLoadResource_thenFail() throws Exception {
        Resource file = new ClassPathResource("test.png");
        when(fileService.loadAsResource(anyString())).thenReturn(file);

        mvc.perform(get("/system/file/image/test.png"))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.UNKNOWN_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("class path resource [test.png] cannot be resolved")));
    }

    @Test
    @WithMockUser
    public void givenTxtFileName_whenLoadResource_thenFail() throws Exception {
        Resource file = new ClassPathResource("test.txt");
        when(fileService.loadAsResource(anyString())).thenReturn(file);

        mvc.perform(get("/system/file/image/test.txt"))
                .andExpect(status().isNotFound());
    }

}
