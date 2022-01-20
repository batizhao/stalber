package me.batizhao.common.core.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 解决 Filter 中抛出异常 @RestControllerAdvice 注解无法捕获的问题
 *
 * @author batizhao
 * @date 2022/1/4
 */
@RestController
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public void handleError(HttpServletRequest request) throws Throwable {
        if (request.getAttribute("javax.servlet.error.exception") != null) {
            throw (Throwable) request.getAttribute("javax.servlet.error.exception");
        }
    }
}
