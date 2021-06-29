package me.batizhao.terrace.exception;

import lombok.Getter;

/**
 * @author batizhao
 * @date 2021/6/29
 */
public class StalberFeignException  extends RuntimeException {

    @Getter
    private int status;

    public StalberFeignException(String message) {
        super(message);
    }

    public StalberFeignException(String message, int status) {
        super(message);
        this.status = status;
    }

}
