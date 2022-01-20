package me.batizhao.common.core.util;

import lombok.Data;
import lombok.experimental.Accessors;
import me.batizhao.common.core.constant.ResultEnum;

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

    public R() {
    }

    public R(Integer code) {
        this.code = code;
        setCode(code);
    }

    public void setCode(Integer code) {
        String message;
        try {
            message = I18nUtil.getMessage("response." + code);
        } catch (Exception e) {
            message = String.valueOf(code);
        }
        this.code = code;
        this.message = message;
    }

    public static <T> R<T> ok() {
        return new R<T>(ResultEnum.SUCCESS.getCode())
                .setData(null);
    }

    public static <T> R<T> ok(T data) {
        return new R<T>(ResultEnum.SUCCESS.getCode())
                .setData(data);
    }

    public static <T> R<T> failed() {
        return new R<T>(ResultEnum.UNKNOWN_ERROR.getCode())
                .setData(null);
    }

    public static <T> R<T> failed(T data) {
        return new R<T>(ResultEnum.UNKNOWN_ERROR.getCode())
                .setData(data);
    }

}
