package me.batizhao.system.unit.service;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.system.domain.Log;
import me.batizhao.system.mapper.LogMapper;
import me.batizhao.system.service.LogService;
import me.batizhao.system.service.impl.LogServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author batizhao
 * @date 2020/9/27
 */
public class LogServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public LogService logService() {
            return new LogServiceImpl();
        }
    }

    @MockBean
    private LogMapper logMapper;

    @Autowired
    private LogService logService;

    private List<Log> logList;
    private Page<Log> logPageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        logList = new ArrayList<>();
        logList.add(new Log().setId(1L).setClassMethod("handleMenuTree4Me").setClassName("me.batizhao.ims.web.MenuController"));
        logList.add(new Log().setId(2L).setClassMethod("handleUserInfo").setClassName("me.batizhao.ims.web.UserController"));

        logPageList = new Page<>();
        logPageList.setRecords(logList);
    }

    @Test
    public void givenNothing_whenFindAllLog_thenSuccess() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Log.class);

        when(logMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(logPageList);

        IPage<Log> logs = logService.findLogs(new Page<>(), new Log());

        assertThat(logs.getRecords(), iterableWithSize(2));
        assertThat(logs.getRecords(), hasItems(hasProperty("className", equalTo("me.batizhao.ims.web.MenuController")),
                hasProperty("className", equalTo("me.batizhao.ims.web.UserController"))));

        logPageList.setRecords(logList.subList(1, 2));
        when(logMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(logPageList);

        logs = logService.findLogs(new Page<>(), new Log().setClassName("lname").setDescription("xxx").setType("success"));
        assertThat(logs.getRecords(), iterableWithSize(1));
        assertThat(logs.getRecords(), hasItems(hasProperty("classMethod", equalTo("handleUserInfo"))));
    }

    @Test
    public void givenLogId_whenFindLog_thenSuccess() {
        when(logMapper.selectById(1L))
                .thenReturn(logList.get(0));

        Log log = logService.findById(1L);

        assertThat(log.getClassMethod(), equalTo("handleMenuTree4Me"));
    }

    @Test
    public void givenLogId_whenFindLog_thenNotFound() {
        when(logMapper.selectById(any()))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> logService.findById(1L));

        verify(logMapper).selectById(any());
    }

}
