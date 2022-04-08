package me.batizhao.system.unit.service;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.system.domain.Dictionary;
import me.batizhao.system.mapper.DictionaryMapper;
import me.batizhao.system.service.DictionaryService;
import me.batizhao.system.service.impl.DictionaryServiceImpl;
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
 * 字典类型
 *
 * @author batizhao
 * @since 2021-02-07
 */
public class DictionaryServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public DictionaryService dictTypeService() {
            return new DictionaryServiceImpl();
        }
    }

    @MockBean
    private DictionaryMapper dictionaryMapper;

    @Autowired
    private DictionaryService dictTypeService;

    private List<Dictionary> dictionaryList;
    private Page<Dictionary> dictTypePageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        dictionaryList = new ArrayList<>();
        dictionaryList.add(new Dictionary().setId(1L).setName("zhangsan"));
        dictionaryList.add(new Dictionary().setId(2L).setName("lisi"));
        dictionaryList.add(new Dictionary().setId(3L).setName("wangwu"));

        dictTypePageList = new Page<>();
        dictTypePageList.setRecords(dictionaryList);
    }

    @Test
    public void givenNothing_whenFindAllDictType_thenSuccess() {
        when(dictionaryMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(dictTypePageList);

        IPage<Dictionary> dictTypes = dictTypeService.findDictionaries(new Page<>(), new Dictionary());

        assertThat(dictTypes.getRecords(), iterableWithSize(3));
        assertThat(dictTypes.getRecords(), hasItems(hasProperty("name", equalTo("zhangsan")),
                hasProperty("name", equalTo("lisi")),
                hasProperty("name", equalTo("wangwu"))));

        dictTypePageList.setRecords(dictionaryList.subList(1, 2));
        when(dictionaryMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(dictTypePageList);

        dictTypes = dictTypeService.findDictionaries(new Page<>(), new Dictionary().setName("lname"));
        assertThat(dictTypes.getRecords(), iterableWithSize(1));
        assertThat(dictTypes.getRecords(), hasItems(hasProperty("name", equalTo("lisi"))));
    }

    @Test
    public void givenDictTypeId_whenFindDictType_thenSuccess() {
        when(dictionaryMapper.selectById(1L))
                .thenReturn(dictionaryList.get(0));

        Dictionary dictionary = dictTypeService.findById(1L);

        assertThat(dictionary.getName(), equalTo("zhangsan"));
    }

    @Test
    public void givenDictTypeId_whenFindDictType_thenNotFound() {
        when(dictionaryMapper.selectById(any()))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> dictTypeService.findById(1L));

        verify(dictionaryMapper).selectById(any());
    }

    @Test
    public void givenDictTypeJson_whenSaveOrUpdateDictType_thenSuccess() {
        Dictionary dictionary_test_data = new Dictionary().setName("zhaoliu");

        // insert 不带 id
        doReturn(1).when(dictionaryMapper).insert(any(Dictionary.class));

        Dictionary dictionary = dictTypeService.saveOrUpdateDictionary(dictionary_test_data);

        verify(dictionaryMapper).insert(any(Dictionary.class));

        // update 需要带 id
        doReturn(1).when(dictionaryMapper).updateById(any(Dictionary.class));

        dictionary = dictTypeService.saveOrUpdateDictionary(dictionaryList.get(0));

        verify(dictionaryMapper).updateById(any(Dictionary.class));
    }

    @Test
    public void givenDictType_whenUpdateStatus_thenSuccess() {
        //Fix can not find lambda cache for this entity
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Dictionary.class);

        doReturn(1).when(dictionaryMapper).update(any(), any(Wrapper.class));
        assertThat(dictTypeService.updateStatus(dictionaryList.get(0)), equalTo(true));

        doReturn(0).when(dictionaryMapper).update(any(), any(Wrapper.class));
        assertThat(dictTypeService.updateStatus(dictionaryList.get(0)), equalTo(false));

        verify(dictionaryMapper, times(2)).update(any(), any(LambdaUpdateWrapper.class));
    }
}