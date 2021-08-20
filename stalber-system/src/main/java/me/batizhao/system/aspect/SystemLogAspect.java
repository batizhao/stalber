package me.batizhao.system.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.annotation.SystemLog;
import me.batizhao.common.util.SecurityUtils;
import me.batizhao.common.util.SpringContextHolder;
import me.batizhao.system.domain.Log;
import me.batizhao.system.event.SystemLogEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * 这里可以使用 Spring @Async 异步 或者 MQ 两种方法发送消息
 *
 * @author batizhao
 * @since 2020-04-01
 **/
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SystemLogAspect {

    private final ObjectMapper objectMapper;

//    private final RocketMQTemplate rocketMQTemplate;

    @Around("@annotation(systemLog)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint point, SystemLog systemLog) {
        Log logDTO = getLog(point, systemLog);
        log.info("@Around System Log is : {}", logDTO);

        long startTime = System.currentTimeMillis();
        Object result;

        try {
            result = point.proceed();
            logDTO.setResult(null != result ? result.toString() : null);
        }
        catch (Exception e) {
            logDTO.setType("failure");
            logDTO.setResult(e.getMessage());
            throw e;
        }
        finally {
            long endTime = System.currentTimeMillis();
            logDTO.setSpend((int) (endTime - startTime));
            SpringContextHolder.publishEvent(new SystemLogEvent(logDTO));
            //rocketMQTemplate.syncSend(MQConstants.TOPIC_SYSTEM_LOG_TAG_COMMON, logDTO);
        }
        return result;
    }

    private Log getLog(JoinPoint point, SystemLog systemLog) {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        Log logDTO = new Log();

        if (StringUtils.isNotBlank(systemLog.value())) {
            logDTO.setDescription(systemLog.value());
        } else if (method.isAnnotationPresent(ApiOperation.class)) {
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            logDTO.setDescription(apiOperation.value());
        } else {
            logDTO.setDescription("请使用 @ApiOperation 或者 @SystemLog 的 value 属性。");
        }

        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        logDTO.setIp(request.getRemoteAddr());
        logDTO.setHttpRequestMethod(request.getMethod());
        logDTO.setClassName(method.getDeclaringClass().getName());
        logDTO.setClassMethod(method.getName());
        logDTO.setParameter(getParameter(method.getParameters(), point.getArgs()));
        logDTO.setClientId("");
        logDTO.setUsername(SecurityUtils.getUser().getUsername());
        logDTO.setUrl(request.getRequestURL().toString());
        return logDTO;
    }

    /**
     * 获取方法参数
     *
     * @param parameters 参数名
     * @param args       参数值
     * @return
     */
    @SneakyThrows
    private String getParameter(Parameter[] parameters, Object[] args) {
        if (ArrayUtils.isEmpty(parameters) || ArrayUtils.isEmpty(args)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            //参数名
            String name = parameters[i].getName();
            //参数值
            Object value = args[i];

            if (null == value || isFilterObject(value)) {
                continue;
            }

            builder.append(name).append("=");
            if (value instanceof String) {
                builder.append(value);
            } else {
                builder.append(objectMapper.writeValueAsString(value));
            }
            if (i < args.length - 1) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    private boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Iterator iter = collection.iterator(); iter.hasNext(); ) {
                return iter.next() instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Iterator iter = map.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iter.next();
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }

}
