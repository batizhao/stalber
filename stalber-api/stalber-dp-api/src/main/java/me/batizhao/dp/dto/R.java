package me.batizhao.dp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 自定义返回实体类
 *
 * @author batizhao
 * @since 2021-07-08
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

    public R(T t) {
        this.data = t;
    }

}
