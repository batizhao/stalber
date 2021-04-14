package me.batizhao.dp.unit.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.dp.domain.Code;
import me.batizhao.dp.domain.CodeMeta;
import me.batizhao.dp.mapper.CodeMapper;
import me.batizhao.dp.service.CodeMetaService;
import me.batizhao.dp.service.CodeService;
import me.batizhao.dp.service.impl.CodeMetaServiceImpl;
import me.batizhao.dp.service.impl.CodeServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author batizhao
 * @since 2020-02-08
 */
@Slf4j
public class CodeServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public CodeService generatorService() {
            return new CodeServiceImpl();
        }
        @Bean
        public CodeMetaService generatorCodeMetaService() {
            return new CodeMetaServiceImpl();
        }
    }

    @MockBean
    private CodeMapper codeMapper;
    @MockBean
    private CodeMetaService codeMetaService;
    @MockBean
    private ObjectMapper objectMapper;

    @Autowired
    private CodeService codeService;

    private List<Code> codeList;
    private Page<Code> codePageList;
    private List<CodeMeta> codeMetaList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        codeMetaList = new ArrayList<>();
        codeMetaList.add(new CodeMeta().setId(1L).setCodeId(1L).setPrimaryKey(true));
        codeMetaList.add(new CodeMeta().setId(2L).setCodeId(1L).setPrimaryKey(false));

        codeList = new ArrayList<>();
        codeList.add(new Code().setId(1L).setTableName("zhangsan").setCodeMetaList(codeMetaList));
        codeList.add(new Code().setId(2L).setTableName("lisi").setClassName("xxx"));
        codeList.add(new Code().setId(3L).setTableName("wangwu"));

        codePageList = new Page<>();
        codePageList.setRecords(codeList);
    }

    @Test
    public void givenNothing_whenFindAllCode_thenSuccess() {
        when(codeMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(codePageList);

        IPage<Code> codes = codeService.findCodes(new Page<>(), new Code());

        assertThat(codes.getRecords(), iterableWithSize(3));
        assertThat(codes.getRecords(), hasItems(hasProperty("tableName", equalTo("zhangsan")),
                hasProperty("tableName", equalTo("lisi")),
                hasProperty("tableName", equalTo("wangwu"))));

        codePageList.setRecords(codeList.subList(1, 2));
        log.info("codePageList: {}", codeList.subList(1, 2));
        when(codeMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(codePageList);

        codes = codeService.findCodes(new Page<>(), new Code().setTableName("lisi"));
        assertThat(codes.getRecords(), iterableWithSize(1));
        assertThat(codes.getRecords(), hasItems(hasProperty("className", equalTo("xxx"))));
    }

    @Test
    public void givenCodeId_whenFindCode_thenSuccess() {
        when(codeMapper.selectById(1L))
                .thenReturn(codeList.get(0));

        Code code = codeService.findById(1L);

        assertThat(code.getTableName(), equalTo("zhangsan"));
    }

    @Test
    public void givenCodeJson_whenSaveOrUpdateCode_thenSuccess() {
        Code code_test_data = new Code().setTableName("zhaoliu").setDsName("ims");

        // insert 不带 id
        doReturn(codeMetaList).when(codeMetaService).findColumnsByTableName(any(String.class), any(String.class));
        doReturn(true).when(codeMetaService).saveBatch(anyList());

        Code code = codeService.saveOrUpdateCode(code_test_data);
        assertThat(code.getDsName(), equalTo("ims"));

        // update 需要带 id
        doReturn(true).when(codeMetaService).updateBatchById(anyList());

        code = codeService.saveOrUpdateCode(codeList.get(0));
        assertThat(code.getTableName(), equalTo("zhangsan"));
    }


}