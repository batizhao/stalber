package me.batizhao.common.util;

import lombok.Data;
import lombok.experimental.Accessors;
import me.batizhao.common.constant.ResultEnum;

import java.io.Serializable;

/**
 * 自定义返回实体类
 *
 * @author batizhao
 * @since 2020-02-20
 */
@Data
@Accessors(chain = true)
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     *
     * @mock 0
     */
    private Integer code;
    /**
     * 错误信息
     *
     * @mock ok
     */
    private String message = "";
    /**
     * 返回结果
     */
    private T data;

    public static <T> R<T> ok() {
        return new R<T>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(null);
    }

    public static <T> R<T> ok(T data) {
        return new R<T>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(data);
    }

    public static <T> R<T> failed() {
        return new R<T>().setCode(ResultEnum.UNKNOWN_ERROR.getCode())
                .setMessage(ResultEnum.UNKNOWN_ERROR.getMessage())
                .setData(null);
    }

    public static <T> R<T> failed(T data) {
        return new R<T>().setCode(ResultEnum.UNKNOWN_ERROR.getCode())
                .setMessage(ResultEnum.UNKNOWN_ERROR.getMessage())
                .setData(data);
    }

}
