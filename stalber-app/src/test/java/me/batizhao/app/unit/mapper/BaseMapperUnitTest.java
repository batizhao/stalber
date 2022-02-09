package me.batizhao.app.unit.mapper;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.junit.jupiter.api.Tag;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

/**
 * 这里最好不要使用 @SpringBootTest，@MybatisTest 保证测试只在 Mapper 层扫描
 * 这里 @MybatisTest 会确保每一个测试方法都是事务回滚的，所以不需要在每个方法上使用 @Transactional
 * 因为 @MybatisTest 只保证加载 Mybatis 自己的类，所以如果想在单元测试中使用 MybatisPlus 的方法，需要声明 @ImportAutoConfiguration
 *
 * @author batizhao
 * @since 2020-02-07
 */
@ActiveProfiles("test")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(MybatisPlusAutoConfiguration.class)
@Tag("integration")
public abstract class BaseMapperUnitTest {

    /**
     * @SpringBootApplication 控制扫描范围，否则会加载额外的包
     * 如果测试包和源码包不一致，这里多了个 unit，还要加上 @MapperScan
     */
    @SpringBootApplication(scanBasePackages = {"me.batizhao.app.mapper"})
    @MapperScan("me.batizhao.app.mapper")
    static class InnerConfig {}

}
