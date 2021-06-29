package me.batizhao.terrace.exception;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author batizhao
 * @date 2021/6/29
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        String json = Util.toString(response.body().asReader(Charset.defaultCharset()));
        Exception exception = new StalberFeignException("An error occurred calling the process API.", response.status());
        log.error("Feign 调用异常，方法：{}, 原始异常：{}", methodKey, json);
        return exception;
    }
}
