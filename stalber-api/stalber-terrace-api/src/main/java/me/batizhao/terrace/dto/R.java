package me.batizhao.terrace.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 流程平台返回包装类
 * 
 * @author batizhao
 * @date 2021/6/11
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
    private String code;

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
