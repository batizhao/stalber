package me.batizhao.ims.unit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.constant.ResultEnum;
import me.batizhao.ims.controller.PostController;
import me.batizhao.ims.domain.Post;
import me.batizhao.ims.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 岗位
 *
 * @author batizhao
 * @since 2021-04-22
 */
@WebMvcTest(PostController.class)
public class PostControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    PostService postService;

    private List<Post> postList;
    private IPage<Post> postPageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        postList = new ArrayList<>();
        postList.add(new Post().setId(1L).setName("zhangsan"));
        postList.add(new Post().setId(2L).setName("lisi"));
        postList.add(new Post().setId(3L).setName("wangwu"));

        postPageList = new Page<>();
        postPageList.setRecords(postList);
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindPosts_thenSuccess() throws Exception {
        when(postService.findPosts(any(Page.class), any(Post.class))).thenReturn(postPageList);

        mvc.perform(get("/ims/posts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data.records", hasSize(3)))
                .andExpect(jsonPath("$.data.records[0].name", equalTo("zhangsan")));

        verify(postService).findPosts(any(Page.class), any(Post.class));
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindAllPost_thenSuccess() throws Exception {
        when(postService.list()).thenReturn(postList);

        mvc.perform(get("/ims/post"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].name", equalTo("zhangsan")));

        verify(postService).list();
    }

    @Test
    @WithMockUser
    public void givenId_whenFindPost_thenSuccess() throws Exception {
        Long id = 1L;

        when(postService.findById(id)).thenReturn(postList.get(0));

        mvc.perform(get("/ims/post/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.name").value("zhangsan"));

        verify(postService).findById(anyLong());
    }

    @Test
    @WithMockUser
    public void givenJson_whenSavePost_thenSuccess() throws Exception {
        Post requestBody = new Post().setName("zhaoliu");

        when(postService.saveOrUpdatePost(any(Post.class)))
                .thenReturn(postList.get(0));

        mvc.perform(post("/ims/post").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        verify(postService).saveOrUpdatePost(any(Post.class));
    }

    @Test
    @WithMockUser
    public void givenJson_whenUpdatePost_thenSuccess() throws Exception {
        Post requestBody = new Post().setId(2L).setName("zhaoliu");

        when(postService.saveOrUpdatePost(any(Post.class)))
                .thenReturn(postList.get(1));

        mvc.perform(post("/ims/post").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(2)));

        verify(postService).saveOrUpdatePost(any(Post.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenDeletePost_thenSuccess() throws Exception {
        when(postService.deleteByIds(anyList())).thenReturn(true);

        mvc.perform(delete("/ims/post").param("ids", "1,2").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(postService).deleteByIds(anyList());
    }

    @Test
    @WithMockUser
    public void givenPost_whenUpdateStatus_thenSuccess() throws Exception {
        Post requestBody = new Post().setId(2L).setStatus("close");

        when(postService.updateStatus(any(Post.class))).thenReturn(true);

        mvc.perform(post("/ims/post/status").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(postService).updateStatus(any(Post.class));
    }
}
