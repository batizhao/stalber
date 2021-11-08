# Stalber  ![](https://img.shields.io/badge/build-passing-brightgreen) ![](https://img.shields.io/badge/coverage-100%25-green)

这是一个 Spring Boot 项目，从 [pecado](https://github.com/batizhao/pecado) 微服务项目简化而来，去掉了配置中心、服务发现、网关、分布式事务、限流降级等框架。

并且，共用 [pecado-vue-ui](https://github.com/batizhao/pecado-vue-ui) 一个前端。

## 环境

* JDK8
* MySQL8
* JUnit5
* Redis6

## 快速开始

- 执行 db/db.sql

- mvn clean install -Dmaven.test.skip=true

- 启动 Redis

- 运行 StalberAdminApplication

  > 启动之前要修改的配置
  >
  > * spring.datasource
  > * spring.redis
  > * pecado.terrace 
  >   * client-id 流程平台id
  >   * client-secret 流程平台secret
  >   * token-store-location 集群环境不要使用 memory，请使用 redis
  > * pecado.code.template-url 相对路径
  >   * 要保证在这个路径下，有代码生成模板
  >   * 如果是开发环境，一般是相对于项目根目录
  >   * 如果是普通 jar 启动，是你启动 java 命令的路径。
  >   * 如果是 Docker 环境，默认的启动路径是 /application，所以要把 /application/templates/pecado 这个路径映射到存储实际放模板的路径上。
  >
  > * pecado.storage
  >   * location 支持 das 和 minio，das 是本地路径，minio 是对象存储
  >   * url 本地路径或者是对象存储的地址
  >
  >
  > 如果要跑单元测试，还有注意 test 下边的这两个配置
  >
  > 如果开发环境不需要验证码，pecado.captcha.enabled: false，同时，前端注释掉验证码输入框。

- Swagger 地址：http://localhost:8888/swagger-ui/

  > 先用下边的方法拿到 access_token，然后使用  `Bearer access_token` 获取授权才能访问 API。
  >
  > ```curl -X POST -d '{"username":"admin","password":"123456"}' -H "Content-type: application/json" http://localhost:8888/uaa/token```


## 建议

- dp\ims\system\commons\admin 五个模块尽量不要修改，方便后续升级。新的业务功能增加新的模块就好了。
- 避免使用 dp\ims\system\commons\admin 命名空间
- oa 模块是整合流程平台的示例

## 开发平台

### 模板管理

遵循以下规范：

* 在 pecado.file.code-template-location 指定模板根目录（可以是任意路径）；
* common、commons 为保留目录，把需要 parse 或者 include 的文件放在这里，不会出现在生成代码清单中。
* 模板文件命名规范为  aaa.bbb.ccc，第一部分描述功能（Controller、Service），bbb 为代码文件类型（java、xml、js、vue），ccc 必须使用 `vm`。

## 数据库版本管理

使用 Liquibase 对数据库版本进行管理。

### Maven 插件常用命令

生成 changelog（需要指定 outputChangeLogFile 属性），默认不包括 data（通过 diffTypes 控制）

```shell
# mvn liquibase:generateChangeLog
```

执行 ChangeSet 升级数据库

```shell
# mvn liquibase:update
```

在当前版本打一个 Tag

```shell
# mvn liquibase:tag -Dliquibase.tag=v1.0
```

回滚到这个 Tag 

```shell
# mvn liquibase:rollback -Dliquibase.rollbackTag=v1.0
```

生成 *v1.0* 以后的 Rollback SQL

```shell
# mvn liquibase:rollbackSQL -Dliquibase.rollbackTag=v1.0
```

回滚 *2020-02-17* 的数据库操作

```shell
# mvn liquibase:rollback -Dliquibase.rollbackDate=2020-02-17
```

### 基本规范

* Liquibase change log 主文件目录 *src/main/resources/db/changelog-master.xml*
* 在 db 下建立子目录 changelog，每个版本建立一个 *changelog-版本号.xml*，如 changelog-1.0.xml

```
.
├── src/main/resources/db
│   ├── changelog                                            
│   │   ├── changelog-1.0.xml                                  
│   │   ├── changelog-1.1.xml
│   │   ├── changelog-1.2.xml
│   ├── changelog-master.xml
│   ├── liquibase.properties
```

* 在 *changelog-master.xml* 中引入目录 *includeAll path="db/changelog/"* 或者单个文件。
* 避免对每个 ChangeSet 做多件事情，以避免失败的自动提交语句使数据库处于意外状态。
* ChangeSet id 使用 *事务描述-年月日-序号*，如 *CreateTableUser-20200214-001, InsertUser-20200214-002*
* ChangeSet 必须填写 *author*
* 所有表，列要加 *remarks* 进行注释
* 严禁修改已经执行过的 ChangeSet 
* 建议为每个 ChangeSet 编写 Rollback，虽然有些 Rollback 会自动生成，但最好自己控制其行为
* 每个版本结束打一个 Tag

## 统一异常处理

* 不在业务代码中捕获任何异常, 全部交由 *@RestControllerAdvice* 来处理

* 统一处理返回类型和消息，使用 *R* 和 *ResultEnum*

## 单元测试

### 单元测试

* 在每个模块的 Mapper、Service、Controller 层都实现了单元测试，类名以 UnitTest 结尾。

* 测试方法名使用 given*Param*\_when*Dothing*\_then*Result* 的方式全名。

* 在每层都会使用 Mockito 隔离所有依赖。

* 需要注意的是尽量控制类和配置加载的范围在当前层。在单元测试中不要使用 *@SpringBootTest*，而是分别使用 *@MybatisTest*、*@WebMvcTest*。

  ```
  在 Spring Security 启用的情况下，如果 Controller 单元测试（使用 @WebMvcTest），隔离 Mapper、Service，要注意以下几点：
  1. post、put、delete 方法要加上 with(csrf())，否则会返回 403
  2. 单元测试要控制扫描范围，防止 Spring Security Config 自动初始化，尤其是 UserDetailsService 自定义的情况（会加载 Mapper）
  3. 测试方法要加上 @WithMockUser，否则会返回 401（OAuth 开启后不需要）
  ```

### API测试

* 在 admin 模块实现了 API 测试，类名以 ApiTest 结尾。
* 在 -P test 情况下才会被激活。
* 在启动测试时，会实例化所有上下文，使用 *@SpringBootTest*。
* 这个测试会调用真正的接口，所有要注意事务的问题，不要生成脏数据。
* 有可能的话，使用专门的测试库（修改测试配置下的 datasource）。
* 还可以在 YApi 中跑集成测试（通过 Swagger 同步接口），并且在其中查看测试报告。
* MockMvc 并不是真正的 Server，所以有些情况并不能完全模拟（比如 ErrorController 4xx Status）。具体可以看这个 [Issues](https://github.com/spring-projects/spring-boot/issues/5574)。

### 测试报告

使用 JaCoCo 生成测试报告，实际使用中，可以集成到 Sonar 质量检查中。

> JaCoCo 不支持接口，所以 Mapper 接口没有被统计进去。

### 本地查看

```
# mvn clean test
```
打开 target/site/jacoco/index.html 可以看到测试报告。

## YApi 整合实践
使用 IDEA EasyYapi 插件，兼容 Swagger，这样如果没有 YApi 或者 EasyYapi 也可以使用 Swagger UI。

同时，可以从代码单向同步接口配置到 YApi，好处是：

* 不至于每次在两边修改后，不好同步。
* 可以删除 YApi 的接口，只要从代码重新同步。
* YApi 的配置算是加入了版本控制，因为所有的配置都在代码里。
* Mock 接口和真实接口的返回数据是一致的。

### POJO 定义

使用 *@ApiModelProperty* 对属性进行说明

如果对属性有细粒度控制，使用 *@mock* 进行定义（[Mock.js](http://mockjs.com/examples.html) 语法）

某些属性会自动生成 mock，如 *@Email、@Min、@Max、@DecimalMax、@DecimalMin* 等，具体看 *.easy.api.config*


```Java
/**
 * @mock @cname
 */
@ApiModelProperty(value = "姓名", example = "张三")
@NotBlank
private String name;

/**
 * @mock @word(3,30)
 */
@ApiModelProperty(value = "用户名", example = "zhangsan")
@NotBlank
@Size(min = 3, max = 30)
private String username;

@ApiModelProperty(value = "邮箱", example = "zhangsan@qq.com")
@NotBlank
@Email
private String email;
```
### 类
```java
/**
 * 接口分类名称
 * 这里是接口分类描述
 *
 * @module 这里是 YApi 项目名称
 *
 */
@Api(tags = "接口分类名称")
@RestController
@RequestMapping("user")
public class UserController
```

### 方法
```java
/**
 * 接口名称
 * 接口备注，接口的详细说明
 *
 * @param username 用户名
 * @return 用户详情
 */
@ApiOperation(value = "接口名称")
@GetMapping("username")
public R<UserInfoVO> handleUsername(@ApiParam(value = "用户名", required = true) @RequestParam @Size(min = 3) String username) {
    User user = userService.findByUsername(username);
    return R.ok(userService.getUserInfo(user.getId()));
}
```

