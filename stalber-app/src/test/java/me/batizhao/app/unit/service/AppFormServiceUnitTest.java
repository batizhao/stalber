package me.batizhao.app.unit.service;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.app.service.AppFormHistoryService;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.app.domain.AppForm;
import me.batizhao.app.mapper.AppFormMapper;
import me.batizhao.app.service.AppFormService;
import me.batizhao.app.service.impl.AppFormServiceImpl;
import org.apache.ibatis.builder.MapperBuilderAssistant;
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
 * 应用表单
 *
 * @author batizhao
 * @since 2022-02-24
 */
public class AppFormServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public AppFormService appFormService() {
            return new AppFormServiceImpl();
        }
    }

    @MockBean
    private AppFormMapper appFormMapper;
    @MockBean
    private AppFormHistoryService appFormHistoryService;

    @Autowired
    private AppFormService appFormService;

    private List<AppForm> appFormList;
    private Page<AppForm> appFormPageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        appFormList = new ArrayList<>();
        appFormList.add(new AppForm().setId(1L).setName("zhangsan"));
        appFormList.add(new AppForm().setId(2L).setName("lisi"));
        appFormList.add(new AppForm().setId(3L).setName("wangwu"));

        appFormPageList = new Page<>();
        appFormPageList.setRecords(appFormList);
    }

    @Test
    public void givenNothing_whenFindAllAppForm_thenSuccess() {
        when(appFormMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(appFormPageList);

        IPage<AppForm> appForms = appFormService.findAppForms(new Page<>(), new AppForm());

        assertThat(appForms.getRecords(), iterableWithSize(3));
        assertThat(appForms.getRecords(), hasItems(hasProperty("name", equalTo("zhangsan")),
                hasProperty("name", equalTo("lisi")),
                hasProperty("name", equalTo("wangwu"))));

        appFormPageList.setRecords(appFormList.subList(1, 2));
        when(appFormMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(appFormPageList);

        appForms = appFormService.findAppForms(new Page<>(), new AppForm().setName("lname"));
        assertThat(appForms.getRecords(), iterableWithSize(1));
        assertThat(appForms.getRecords(), hasItems(hasProperty("name", equalTo("lisi"))));
    }

    @Test
    public void givenAppFormId_whenFindAppForm_thenSuccess() {
        when(appFormMapper.selectById(1L))
                .thenReturn(appFormList.get(0));

        AppForm appForm = appFormService.findById(1L);

        assertThat(appForm.getName(), equalTo("zhangsan"));
    }

    @Test
    public void givenAppFormId_whenFindAppForm_thenNotFound() {
        when(appFormMapper.selectById(any()))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> appFormService.findById(1L));

        verify(appFormMapper).selectById(any());
    }

    @Test
    public void givenAppFormJson_whenSaveOrUpdateAppForm_thenSuccess() {
        AppForm appForm_test_data = new AppForm().setName("zhaoliu");

        // insert 不带 id
        doReturn(1).when(appFormMapper).insert(any(AppForm.class));

        appFormService.saveOrUpdateAppForm(appForm_test_data);

        verify(appFormMapper).insert(any(AppForm.class));

        // update 需要带 id
        doReturn(1).when(appFormMapper).updateById(any(AppForm.class));

        appFormService.saveOrUpdateAppForm(appFormList.get(0));

        verify(appFormMapper).updateById(any(AppForm.class));
    }

    @Test
    public void givenAppForm_whenUpdateStatus_thenSuccess() {
        //Fix can not find lambda cache for this entity
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), AppForm.class);

        doReturn(1).when(appFormMapper).update(any(), any(Wrapper.class));
        assertThat(appFormService.updateStatus(appFormList.get(0)), equalTo(true));

        doReturn(0).when(appFormMapper).update(any(), any(Wrapper.class));
        assertThat(appFormService.updateStatus(appFormList.get(0)), equalTo(false));

        verify(appFormMapper, times(2)).update(any(), any(LambdaUpdateWrapper.class));
    }
}