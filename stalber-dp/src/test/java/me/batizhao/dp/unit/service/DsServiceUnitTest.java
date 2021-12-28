package me.batizhao.dp.unit.service;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.common.core.exception.DataSourceException;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.common.core.util.SpringContextHolder;
import me.batizhao.dp.domain.Ds;
import me.batizhao.dp.mapper.DsMapper;
import me.batizhao.dp.service.DsService;
import me.batizhao.dp.service.impl.DsServiceImpl;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

/**
 * 数据源
 *
 * @author batizhao
 * @since 2020-10-19
 */
public class DsServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public DsService dsService() {
            return new DsServiceImpl();
        }
    }

    @MockBean
    private DsMapper dsMapper;
    @MockBean
    private DataSourceCreator dataSourceCreator;
    @MockBean
    private StringEncryptor stringEncryptor;

    @Autowired
    private DsService dsService;

    @Autowired
    @SpyBean
    private DsService dsService2;

    private List<Ds> dsList;
    private Page<Ds> dsPageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        dsList = new ArrayList<>();
        dsList.add(new Ds().setId(1).setUsername("zhangsan").setPassword("xxx").setUrl("aaa").setName("bbb"));
        dsList.add(new Ds().setId(2).setUsername("lisi").setName("lname"));
        dsList.add(new Ds().setId(3).setUsername("wangwu").setName("wname"));

        dsPageList = new Page<>();
        dsPageList.setRecords(dsList);
    }

    @Test
    public void givenNothing_whenFindAllDs_thenSuccess() {
        when(dsMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(dsPageList);

        IPage<Ds> dss = dsService.findDss(new Page<>(), new Ds());

        assertThat(dss.getRecords(), iterableWithSize(3));
        assertThat(dss.getRecords(), hasItems(hasProperty("username", equalTo("zhangsan")),
                hasProperty("username", equalTo("lisi")),
                hasProperty("username", equalTo("wangwu"))));

        dsPageList.setRecords(dsList.subList(1, 2));
        when(dsMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(dsPageList);

        dss = dsService.findDss(new Page<>(), new Ds().setName("lname"));
        assertThat(dss.getRecords(), iterableWithSize(1));
        assertThat(dss.getRecords(), hasItems(hasProperty("username", equalTo("lisi"))));

        dsPageList.setRecords(dsList.subList(2, 3));
        when(dsMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(dsPageList);
        dss = dsService.findDss(new Page<>(), new Ds().setUsername("wangwu"));
        assertThat(dss.getRecords(), iterableWithSize(1));
        assertThat(dss.getRecords(), hasItems(hasProperty("name", equalTo("wname"))));
    }

    @Test
    public void givenDsId_whenFindDs_thenSuccess() {
        when(dsMapper.selectById(1))
                .thenReturn(dsList.get(0));

        Ds ds = dsService.findById(1);

        assertThat(ds.getUsername(), equalTo("zhangsan"));
    }

    @Test
    public void givenDsId_whenFindDs_thenFail() {
        when(dsMapper.selectById(anyInt()))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> dsService.findById(1));

        verify(dsMapper).selectById(anyInt());
    }

    /**
     * 类方法内部调用 Mock
     */
    @Test
    public void givenDsJson_whenSaveOrUpdateDs_thenSuccess() {
        Ds ds_test_data = new Ds().setUsername("zhaoliu").setUrl("xxx").setPassword("xxx");

        doReturn(true).when(dsService2).checkDataSource(any(Ds.class));
        doNothing().when(dsService2).addDynamicDataSource(any(Ds.class));

        // insert 不带 id
        doReturn(1).when(dsMapper).insert(any(Ds.class));
        doReturn("vxth7ibr6hlxbE362qiQGYtiGWOotkYp").when(stringEncryptor).encrypt(anyString());

        dsService2.saveOrUpdateDs(ds_test_data);

        verify(dsMapper).insert(any(Ds.class));

        try (MockedStatic<SpringContextHolder> mockStatic = mockStatic(SpringContextHolder.class)) {
            DynamicRoutingDataSource dataSource = spy(DynamicRoutingDataSource.class);
            mockStatic.when(() -> {
                SpringContextHolder.getBean(DynamicRoutingDataSource.class);
            }).thenReturn(dataSource);

            doReturn(dsList.get(0)).when(dsService2).findById(anyInt());
            doNothing().when(dataSource).removeDataSource(dsList.get(0).getName());

            // update 需要带 id
            doReturn(1).when(dsMapper).updateById(any(Ds.class));

            dsService2.saveOrUpdateDs(dsList.get(0));

            verify(dsMapper).updateById(any(Ds.class));
        }
    }

    @Test
    public void givenDs_whenAddDynamicDataSource_thenSuccess() {
        try (MockedStatic<SpringContextHolder> mockStatic = mockStatic(SpringContextHolder.class)) {
            DataSource dataSource = spy(DataSource.class);
            DynamicRoutingDataSource routingDataSource = spy(new DynamicRoutingDataSource());

            mockStatic.when(() -> {
                SpringContextHolder.getBean(DynamicRoutingDataSource.class);
            }).thenReturn(routingDataSource);

            when(dataSourceCreator.createDataSource(any(DataSourceProperty.class)))
                    .thenReturn(dataSource);

            doNothing().when(routingDataSource).addDataSource(anyString(), any(DataSource.class));

            dsService.addDynamicDataSource(dsList.get(0));

            verify(dataSourceCreator).createDataSource(any(DataSourceProperty.class));
            verify(routingDataSource).addDataSource(anyString(), any(DataSource.class));
        }
    }

    @Test
    public void givenDs_whenCheckDataSource_thenSuccess() {
        try (MockedStatic<DriverManager> mockStatic = mockStatic(DriverManager.class)) {
            Connection connection = spy(Connection.class);
            mockStatic.when(() -> {
                DriverManager.getConnection(anyString(), anyString(), anyString());
            }).thenReturn(connection);

            dsService.checkDataSource(dsList.get(0));

            mockStatic.verify(() -> {
                DriverManager.getConnection(anyString(), anyString(), anyString());
            });
        }
    }

    @Test
    public void givenDs_whenCheckDataSource_thenFail() {
        try (MockedStatic<DriverManager> mockStatic = mockStatic(DriverManager.class)) {
            mockStatic.when(() -> {
                DriverManager.getConnection(anyString(), anyString(), anyString());
            }).thenThrow(SQLException.class);

            assertThrows(DataSourceException.class, () -> dsService.checkDataSource(dsList.get(0)));
        }
    }

    @Test
    public void givenDs_whenUpdateStatus_thenSuccess() {
        //Fix can not find lambda cache for this entity
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Ds.class);

        doReturn(1).when(dsMapper).update(any(), any(Wrapper.class));
        assertThat(dsService.updateStatus(dsList.get(0)), equalTo(true));

        doReturn(0).when(dsMapper).update(any(), any(Wrapper.class));
        assertThat(dsService.updateStatus(dsList.get(0)), equalTo(false));

        verify(dsMapper, times(2)).update(any(), any(LambdaUpdateWrapper.class));
    }

}