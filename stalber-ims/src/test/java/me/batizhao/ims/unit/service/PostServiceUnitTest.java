package me.batizhao.ims.unit.service;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.ims.domain.Post;
import me.batizhao.ims.mapper.PostMapper;
import me.batizhao.ims.service.PostService;
import me.batizhao.ims.service.UserPostService;
import me.batizhao.ims.service.impl.PostServiceImpl;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

/**
 * 岗位
 *
 * @author batizhao
 * @since 2021-04-22
 */
public class PostServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public PostService postService() {
            return new PostServiceImpl();
        }
    }

    @MockBean
    private PostMapper postMapper;
    @MockBean
    private UserPostService userPostService;

    @Autowired
    private PostService postService;

    @MockBean
    private ServiceImpl service;

    private List<Post> postList;
    private Page<Post> postPageList;

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
    public void givenNothing_whenFindAllPost_thenSuccess() {
        when(postMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(postPageList);

        IPage<Post> posts = postService.findPosts(new Page<>(), new Post());

        assertThat(posts.getRecords(), iterableWithSize(3));
        assertThat(posts.getRecords(), hasItems(hasProperty("name", equalTo("zhangsan")),
                hasProperty("name", equalTo("lisi")),
                hasProperty("name", equalTo("wangwu"))));

        postPageList.setRecords(postList.subList(1, 2));
        when(postMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(postPageList);

        posts = postService.findPosts(new Page<>(), new Post().setName("lname"));
        assertThat(posts.getRecords(), iterableWithSize(1));
        assertThat(posts.getRecords(), hasItems(hasProperty("name", equalTo("lisi"))));
    }

    @Test
    public void givenPostId_whenFindPost_thenSuccess() {
        when(postMapper.selectById(1L))
                .thenReturn(postList.get(0));

        Post post = postService.findById(1L);

        assertThat(post.getName(), equalTo("zhangsan"));
    }

    @Test
    public void givenPostId_whenFindPost_thenNotFound() {
        when(postMapper.selectById(any()))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> postService.findById(1L));

        verify(postMapper).selectById(any());
    }

    @Test
    public void givenPostJson_whenSaveOrUpdatePost_thenSuccess() {
        Post post_test_data = new Post().setName("zhaoliu");

        // insert 不带 id
        doReturn(1).when(postMapper).insert(any(Post.class));

        postService.saveOrUpdatePost(post_test_data);

        verify(postMapper).insert(any(Post.class));

        // update 需要带 id
        doReturn(1).when(postMapper).updateById(any(Post.class));

        postService.saveOrUpdatePost(postList.get(0));

        verify(postMapper).updateById(any(Post.class));
    }

    @Test
    public void givenIds_whenDelete_thenSuccess() {
        doReturn(true).when(service).removeByIds(anyList());
        doReturn(true).when(userPostService).remove(any(Wrapper.class));

        Boolean b = postService.deleteByIds(Arrays.asList(1L, 2L));
        assertThat(b, equalTo(true));
    }

    @Test
    public void givenPost_whenUpdateStatus_thenSuccess() {
        //Fix can not find lambda cache for this entity
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Post.class);

        doReturn(1).when(postMapper).update(any(), any(Wrapper.class));
        assertThat(postService.updateStatus(postList.get(0)), equalTo(true));

        doReturn(0).when(postMapper).update(any(), any(Wrapper.class));
        assertThat(postService.updateStatus(postList.get(0)), equalTo(false));

        verify(postMapper, times(2)).update(any(), any(LambdaUpdateWrapper.class));
    }
}