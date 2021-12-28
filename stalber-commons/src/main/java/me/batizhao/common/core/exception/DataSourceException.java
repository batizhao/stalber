package me.batizhao.common.core.exception;

/**
 * @author batizhao
 * @date 2020/9/23
 */
public class DataSourceException extends RuntimeException {

    public DataSourceException(String message) {
        super(message);
    }

    public DataSourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
