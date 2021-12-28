package me.batizhao.common.core.constant;

/**
 * 定义返回码的枚举类
 *
 * 返回消息码：两位应用编码（统一编排，通常对应 boot 应用，10-99） + 两位模块编码（00为通用错误） + 两位错误编码（01-99）
 * 如当前应用编码为10，发生了"缺少请求体"的错误，这是一个通用错误（00），所以是 1000，再加上错误编码（01），最后是 100001
 * 系统级错误 10
 *
 * @author batizhao
 * @since 2020-02-20
 */
public enum ResultEnum {

    /**
     * 成功
     */
    SUCCESS(0),

    /**
     * 系统错误
     */
    UNKNOWN_ERROR(100000),
    PARAMETER_INVALID(100001),
    OAUTH2_TOKEN_ERROR(100002),
    OAUTH2_TOKEN_INVALID(100003),
    PERMISSION_UNAUTHORIZED_ERROR(100004),
    PERMISSION_FORBIDDEN_ERROR(100005),
    GATEWAY_ERROR(100008),
    TOO_MANY_REQUEST(100009),
    RESOURCE_NOT_FOUND(100010),
    MQ_MESSAGE_ERROR(100011),

    /**
     * IMS 01 模块错误
     */
    IMS_USER_NOT_FOUND(100100),

    /**
     * SYSTEM 02 模块错误
     */
    SYSTEM_STORAGE_ERROR(100200),

    /**
     * DP 03 模块错误
     */
    DP_DS_ERROR(100300);

    private final Integer code;

    ResultEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
