package me.batizhao.ims.unit.mapper;

import me.batizhao.ims.domain.Post;
import me.batizhao.ims.mapper.PostMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * 岗位
 *
 * @author batizhao
 * @since 2021-04-22
 */
public class PostMapperUnitTest extends BaseMapperUnitTest {

    @Autowired
    PostMapper postMapper;

    @Test
    public void testFindRolesByUserId() {
        List<Post> posts = postMapper.findPostsByUserId(1L);

        assertThat(posts.size(), is(0));
    }

}
