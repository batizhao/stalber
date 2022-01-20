package me.batizhao.dp.unit.service;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.dp.domain.Form;
import me.batizhao.dp.mapper.FormMapper;
import me.batizhao.dp.service.FormHistoryService;
import me.batizhao.dp.service.FormService;
import me.batizhao.dp.service.impl.FormServiceImpl;
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
 * 表单
 *
 * @author batizhao
 * @since 2021-03-08
 */
public class FormServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public FormService formService() {
            return new FormServiceImpl();
        }
    }

    @MockBean
    private FormMapper formMapper;
    @MockBean
    private FormHistoryService formHistoryService;

    @Autowired
    private FormService formService;

    private List<Form> formList;
    private Page<Form> formPageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        formList = new ArrayList<>();
        formList.add(new Form().setId(1L).setName("zhangsan"));
        formList.add(new Form().setId(2L).setName("lisi"));
        formList.add(new Form().setId(3L).setName("wangwu"));

        formPageList = new Page<>();
        formPageList.setRecords(formList);
    }

    @Test
    public void givenNothing_whenFindAllForm_thenSuccess() {
        when(formMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(formPageList);

        IPage<Form> forms = formService.findForms(new Page<>(), new Form());

        assertThat(forms.getRecords(), iterableWithSize(3));
        assertThat(forms.getRecords(), hasItems(hasProperty("name", equalTo("zhangsan")),
                hasProperty("name", equalTo("lisi")),
                hasProperty("name", equalTo("wangwu"))));

        formPageList.setRecords(formList.subList(1, 2));
        when(formMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(formPageList);

        forms = formService.findForms(new Page<>(), new Form().setName("lname"));
        assertThat(forms.getRecords(), iterableWithSize(1));
        assertThat(forms.getRecords(), hasItems(hasProperty("name", equalTo("lisi"))));
    }

    @Test
    public void givenFormId_whenFindForm_thenSuccess() {
        when(formMapper.selectById(1L))
                .thenReturn(formList.get(0));

        Form form = formService.findById(1L);

        assertThat(form.getName(), equalTo("zhangsan"));
    }

    @Test
    public void givenFormId_whenFindForm_thenNotFound() {
        when(formMapper.selectById(any()))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> formService.findById(1L));

        verify(formMapper).selectById(any());
    }

    @Test
    public void givenFormJson_whenSaveOrUpdateForm_thenSuccess() {
        Form form_test_data = new Form().setName("zhaoliu");

        // insert 不带 id
        doReturn(1).when(formMapper).insert(any(Form.class));

        formService.saveOrUpdateForm(form_test_data);

        verify(formMapper).insert(any(Form.class));

        // update 需要带 id
        doReturn(1).when(formMapper).updateById(any(Form.class));

        formService.saveOrUpdateForm(formList.get(0));

        verify(formMapper).updateById(any(Form.class));
    }

    @Test
    public void givenForm_whenUpdateStatus_thenSuccess() {
        //Fix can not find lambda cache for this entity
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Form.class);

        doReturn(1).when(formMapper).update(any(), any(Wrapper.class));
        assertThat(formService.updateStatus(formList.get(0)), equalTo(true));

        doReturn(0).when(formMapper).update(any(), any(Wrapper.class));
        assertThat(formService.updateStatus(formList.get(0)), equalTo(false));

        verify(formMapper, times(2)).update(any(), any(LambdaUpdateWrapper.class));
    }
}