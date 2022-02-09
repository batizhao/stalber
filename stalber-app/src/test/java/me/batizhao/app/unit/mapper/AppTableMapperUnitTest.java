package me.batizhao.app.unit.mapper;

import me.batizhao.app.mapper.AppTableMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 应用表
 *
 * @author batizhao
 * @since 2022-01-27
 */
public class AppTableMapperUnitTest extends BaseMapperUnitTest {

    @Autowired
    AppTableMapper appTableMapper;

    @Test
    public void testFindRolesByUserId() {
        appTableMapper.createTable("CREATE TABLE tname (id bigint NOT NULL)");
    }

}
