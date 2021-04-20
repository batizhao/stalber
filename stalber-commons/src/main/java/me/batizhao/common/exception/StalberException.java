package me.batizhao.common.exception;

/**
 * @author batizhao
 * @since 2020-03-20
 **/
public class StalberException extends RuntimeException {

    public StalberException(String message) {
        super(message);
    }

    public StalberException(String message, Throwable cause) {
        super(message, cause);
    }
}
