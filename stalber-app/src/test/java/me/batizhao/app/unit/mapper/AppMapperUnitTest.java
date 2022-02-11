package me.batizhao.app.unit.mapper;

import me.batizhao.app.mapper.AppMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 应用表
 *
 * @author batizhao
 * @since 2022-01-27
 */
public class AppMapperUnitTest extends BaseMapperUnitTest {

    @Autowired
    AppMapper appMapper;

    @Test
    public void testFindRolesByUserId() {
        appMapper.createTable("CREATE TABLE tname (id bigint NOT NULL)");
    }

}
