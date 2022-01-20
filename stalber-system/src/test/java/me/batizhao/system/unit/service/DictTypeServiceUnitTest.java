package me.batizhao.system.unit.service;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.system.domain.DictType;
import me.batizhao.system.mapper.DictTypeMapper;
import me.batizhao.system.service.DictDataService;
import me.batizhao.system.service.DictTypeService;
import me.batizhao.system.service.impl.DictTypeServiceImpl;
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
public class DictTypeServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public DictTypeService dictTypeService() {
            return new DictTypeServiceImpl();
        }
    }

    @MockBean
    private DictTypeMapper dictTypeMapper;
    @MockBean
    private DictDataService dictDataService;

    @Autowired
    private DictTypeService dictTypeService;

    private List<DictType> dictTypeList;
    private Page<DictType> dictTypePageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        dictTypeList = new ArrayList<>();
        dictTypeList.add(new DictType().setId(1L).setName("zhangsan"));
        dictTypeList.add(new DictType().setId(2L).setName("lisi"));
        dictTypeList.add(new DictType().setId(3L).setName("wangwu"));

        dictTypePageList = new Page<>();
        dictTypePageList.setRecords(dictTypeList);
    }

    @Test
    public void givenNothing_whenFindAllDictType_thenSuccess() {
        when(dictTypeMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(dictTypePageList);

        IPage<DictType> dictTypes = dictTypeService.findDictTypes(new Page<>(), new DictType());

        assertThat(dictTypes.getRecords(), iterableWithSize(3));
        assertThat(dictTypes.getRecords(), hasItems(hasProperty("name", equalTo("zhangsan")),
                hasProperty("name", equalTo("lisi")),
                hasProperty("name", equalTo("wangwu"))));

        dictTypePageList.setRecords(dictTypeList.subList(1, 2));
        when(dictTypeMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(dictTypePageList);

        dictTypes = dictTypeService.findDictTypes(new Page<>(), new DictType().setName("lname"));
        assertThat(dictTypes.getRecords(), iterableWithSize(1));
        assertThat(dictTypes.getRecords(), hasItems(hasProperty("name", equalTo("lisi"))));
    }

    @Test
    public void givenDictTypeId_whenFindDictType_thenSuccess() {
        when(dictTypeMapper.selectById(1L))
                .thenReturn(dictTypeList.get(0));

        DictType dictType = dictTypeService.findById(1L);

        assertThat(dictType.getName(), equalTo("zhangsan"));
    }

    @Test
    public void givenDictTypeId_whenFindDictType_thenNotFound() {
        when(dictTypeMapper.selectById(any()))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> dictTypeService.findById(1L));

        verify(dictTypeMapper).selectById(any());
    }

    @Test
    public void givenDictTypeJson_whenSaveOrUpdateDictType_thenSuccess() {
        DictType dictType_test_data = new DictType().setName("zhaoliu");

        // insert 不带 id
        doReturn(1).when(dictTypeMapper).insert(any(DictType.class));

        DictType dictType = dictTypeService.saveOrUpdateDictType(dictType_test_data);

        verify(dictTypeMapper).insert(any(DictType.class));

        // update 需要带 id
        doReturn(1).when(dictTypeMapper).updateById(any(DictType.class));

        dictType = dictTypeService.saveOrUpdateDictType(dictTypeList.get(0));

        verify(dictTypeMapper).updateById(any(DictType.class));
    }

    @Test
    public void givenDictType_whenUpdateStatus_thenSuccess() {
        //Fix can not find lambda cache for this entity
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), DictType.class);

        doReturn(1).when(dictTypeMapper).update(any(), any(Wrapper.class));
        assertThat(dictTypeService.updateStatus(dictTypeList.get(0)), equalTo(true));

        doReturn(0).when(dictTypeMapper).update(any(), any(Wrapper.class));
        assertThat(dictTypeService.updateStatus(dictTypeList.get(0)), equalTo(false));

        verify(dictTypeMapper, times(2)).update(any(), any(LambdaUpdateWrapper.class));
    }
}