package me.batizhao;

import me.batizhao.admin.StalberAdminApplication;
import me.batizhao.common.exception.WebExceptionHandler;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 在集成测试中，不再需要 Mock Bean 和 Stub，
 * 但是需要实例化整个上下文，再对返回数据进行判断
 * 参数校验相关的测试可以放在集成测试中，因为不需要 Stub Data
 * Import WebExceptionHandler 是因为 mvn test 不能加载应用外的 resource
 *
 * @author batizhao
 * @since 2020-02-07
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = StalberAdminApplication.class)
@AutoConfigureMockMvc
@Import(WebExceptionHandler.class)
@ActiveProfiles("test")
@Tag("api")
public abstract class BaseApiTest {

    /**
     * 使用一个超长时间的 token，隔离获取 token 的操作。避免测试 token 过期！
     * curl -X POST --user 'test:passw0rd' -d 'grant_type=password&username=admin&password=123456' http://localhost:4000/oauth/token
     * curl -X POST --user 'test:passw0rd' -d 'grant_type=password&username=tom&password=123456' http://localhost:4000/oauth/token
     */
    @Value("${pecado.token.admin}")
    public String adminAccessToken;
    @Value("${pecado.token.user}")
    public String userAccessToken;

    @Autowired
    public MockMvc mvc;

    /**
     * <p>
     * 这里要注意，如果在 @Async 中使用了 Feign 调用（@SystemLog），在集成测试时会产生一个问题。
     *
     * - 这个问题只在 JUnit 测试时才会出现
     * - 如果测试运行时间很短，有可能在主线程退出时，Ribbon 调度任务还未完成；
     * - 因为 Feign 会使用 Ribbon 调用 PollingServerListUpdater，启动定时任务 ScheduledThreadPoolExecutor 去 Nacos 注册中心拿配置（默认延时 1S）
     * - 如果不阻塞主线程，就会导致 Ribbon 拿不到 ServerList；
     * - 常规的使用 WaitForTasksToCompleteOnShutdown 和 AwaitTerminationSeconds 实现优雅关闭线程方法对 Ribbon 的 ScheduledThreadPoolExecutor 不起作用，
     *   只对 Spring 自己的 ThreadPoolTaskScheduler 起作用
     * - 阻止测试主线程提前退出，要在 JUnit 中使用下边的方法，在每个测试方法主线程结束后阻塞一段时间
     * </p>
     */
//    @SneakyThrows
//    @AfterEach
//    public void sleep() {
//        Thread.sleep(5000L);
//    }
}
