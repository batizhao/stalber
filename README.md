# Stalber  ![](https://img.shields.io/badge/build-passing-brightgreen) ![](https://img.shields.io/badge/coverage-100%25-green)

这是一个 Spring Boot 项目，从 [pecado](https://github.com/batizhao/pecado) 微服务项目简化而来，去掉了配置中心、服务发现、网关、分布式事务、限流降级等框架。

并且，共用 [pecado-vue-ui](https://github.com/batizhao/pecado-vue-ui) 一个前端。

## 环境

* JDK8
* MySQL8
* JUnit4
* Hamcrest2

## 快速开始

- 执行 db/db.sql
- mvn clean install -Dmaven.test.skip=true
- 运行 StalberAdminApplication

## 建议

- dp\ims\system\commons\admin 五个模块尽量不要修改，方便后续升级。新的业务功能增加新的模块就好了。
- 避免使用 dp\ims\system 三个命名空间

## 认证和授权

### 获取 access_token
```shell
# curl -X POST --user 'client_app:123456' -d 'grant_type=password&username=admin&password=123456' http://localhost:8080/oauth/token
{
  "access_token" : "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE1ODMwNDk0NTksImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwianRpIjoiMDkyZjQ3OGMtNjM5MC00NTdhLWExOTItMGRlZmZiYmUzYTk2IiwiY2xpZW50X2lkIjoiY2xpZW50X2FwcCIsInVzZXJuYW1lIjoiYWRtaW4ifQ.JVFPmDJ6NP6zuErN9gN9MhgxlLsCFUn-JPc_9u-ZOj0",
  "token_type" : "bearer",
  "refresh_token" : "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJhdGkiOiIwOTJmNDc4Yy02MzkwLTQ1N2EtYTE5Mi0wZGVmZmJiZTNhOTYiLCJleHAiOjE1ODM5MDk4NTksImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwianRpIjoiMzZlNDgzNmEtMzM4OC00YWJhLWFiMmEtMTRkODMwZTJjZDJlIiwiY2xpZW50X2lkIjoiY2xpZW50X2FwcCIsInVzZXJuYW1lIjoiYWRtaW4ifQ.ByoaXSa6HIJc0OfmVx47SMPt_OmyrD7T9E_kxhtrd20",
  "expires_in" : 3599,
  "scope" : "all",
  "username" : "admin",
  "jti" : "092f478c-6390-457a-a192-0deffbbe3a96"
}
```

### 使用 refresh_token 更新 access_token

* 当使用一个过期的 *access_token* 访问 API 时，服务端会返回 100003（**Access token expired**）。测试用例 *givenExpiredToken_whenGetSecureRequest_thenUnauthorized*。
* 前端捕获这个 code，用本地存储的 *refresh_token* 去换取新的 *access_token*。
* 如果 *refresh_token* 也过期，服务端会返回 100002（**Invalid refresh token**），重新输入账号密码请求 *access_token*。测试用例 *givenInvalidRefreshToken_whenGetAccessToken_thenOAuthException*

```shell
# curl -X POST --user 'client_app:123456' 'localhost:8080/oauth/token?grant_type=refresh_token&refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJhdGkiOiIwOTJmNDc4Yy02MzkwLTQ1N2EtYTE5Mi0wZGVmZmJiZTNhOTYiLCJleHAiOjE1ODM5MDk4NTksImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwianRpIjoiMzZlNDgzNmEtMzM4OC00YWJhLWFiMmEtMTRkODMwZTJjZDJlIiwiY2xpZW50X2lkIjoiY2xpZW50X2FwcCIsInVzZXJuYW1lIjoiYWRtaW4ifQ.ByoaXSa6HIJc0OfmVx47SMPt_OmyrD7T9E_kxhtrd20'
```

### 调用 API
```shell
# curl -H "authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE1ODMwNDk0NTksImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwianRpIjoiMDkyZjQ3OGMtNjM5MC00NTdhLWExOTItMGRlZmZiYmUzYTk2IiwiY2xpZW50X2lkIjoiY2xpZW50X2FwcCIsInVzZXJuYW1lIjoiYWRtaW4ifQ.JVFPmDJ6NP6zuErN9gN9MhgxlLsCFUn-JPc_9u-ZOj0"  localhost:8080/user/1
{
  "code" : 0,
  "message" : "ok",
  "data" : {
    "id" : 1,
    "username" : "admin",
    "password" : "$2a$10$rFoOrbWD2p.1CjBoBqTeaOUgpxFmtZknsDEvF78AsMXvsxU1AyAZu",
    "email" : "admin@qq.com",
    "name" : "系统管理员",
    "time" : "2016-09-29 10:00:00"
  }
}
```

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

* *@RestControllerAdvice* 不会处理 404 异常，所以必须要单独处理，示例 *ErrorHandler*

* 统一处理返回类型和消息，使用 *R* 和 *ResultEnum*

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
public R<User> findByUsername(@ApiParam(value = "用户名", required = true)
                                         @RequestParam @Size(min = 3) String username) {
    User user = userService.findByUsername(username);
    return new R<User>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(user);
}
```

