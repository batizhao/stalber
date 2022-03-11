package me.batizhao.app.unit.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.app.domain.AppTable;
import me.batizhao.app.mapper.AppTableMapper;
import me.batizhao.app.service.AppFormService;
import me.batizhao.app.service.AppService;
import me.batizhao.app.service.AppTableService;
import me.batizhao.app.service.impl.AppTableServiceImpl;
import me.batizhao.common.core.config.CodeProperties;
import me.batizhao.common.core.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

/**
 * 应用表
 *
 * @author batizhao
 * @since 2022-01-27
 */
public class AppTableServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public AppTableService appTableService() {
            return new AppTableServiceImpl();
        }
    }

    @MockBean
    private AppTableMapper appTableMapper;
    @MockBean
    private AppService appService;
    @MockBean
    private AppFormService appFormService;
    @MockBean
    private CodeProperties codeProperties;
    @MockBean
    private ObjectMapper objectMapper;

    @Autowired
    private AppTableService appTableService;

    private List<AppTable> appTableList;
    private Page<AppTable> appTablePageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        appTableList = new ArrayList<>();
        appTableList.add(new AppTable().setId(1L).setTableName("zhangsan"));
        appTableList.add(new AppTable().setId(2L).setTableName("lisi"));
        appTableList.add(new AppTable().setId(3L).setTableName("wangwu"));

        appTablePageList = new Page<>();
        appTablePageList.setRecords(appTableList);
    }

    @Test
    public void givenNothing_whenFindAllAppTable_thenSuccess() {
        when(appTableMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(appTablePageList);

        IPage<AppTable> appTables = appTableService.findAppTables(new Page<>(), new AppTable());

        assertThat(appTables.getRecords(), iterableWithSize(3));
        assertThat(appTables.getRecords(), hasItems(hasProperty("tableName", equalTo("zhangsan")),
                hasProperty("tableName", equalTo("lisi")),
                hasProperty("tableName", equalTo("wangwu"))));

        appTablePageList.setRecords(appTableList.subList(1, 2));
        when(appTableMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(appTablePageList);

        appTables = appTableService.findAppTables(new Page<>(), new AppTable().setTableName("lname"));
        assertThat(appTables.getRecords(), iterableWithSize(1));
        assertThat(appTables.getRecords(), hasItems(hasProperty("tableName", equalTo("lisi"))));
    }

    @Test
    public void givenAppTableId_whenFindAppTable_thenSuccess() {
        when(appTableMapper.selectById(1L))
                .thenReturn(appTableList.get(0));

        AppTable appTable = appTableService.findById(1L);

        assertThat(appTable.getTableName(), equalTo("zhangsan"));
    }

    @Test
    public void givenAppTableId_whenFindAppTable_thenNotFound() {
        when(appTableMapper.selectById(any()))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> appTableService.findById(1L));

        verify(appTableMapper).selectById(any());
    }

    @Test
    public void givenAppTableJson_whenSaveOrUpdateAppTable_thenSuccess() {
        AppTable appTable = new AppTable().setTableComment("测试表").setTableName("tname").setAppId(1L).setDsName("ims");

        // insert 不带 id
        doReturn(1).when(appTableMapper).insert(any(AppTable.class));

        appTableService.saveOrUpdateAppTable(appTable);

        verify(appTableMapper).insert(any(AppTable.class));

        // update 需要带 id
        doReturn(1).when(appTableMapper).updateById(any(AppTable.class));

        appTableService.saveOrUpdateAppTable(appTableList.get(0));

        verify(appTableMapper).updateById(any(AppTable.class));
    }
}