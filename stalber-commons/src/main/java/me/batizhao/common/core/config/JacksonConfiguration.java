package me.batizhao.common.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.constant.PecadoConstants;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author batizhao
 * @date 2020-06-29
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.locale(Locale.CHINA);
            builder.indentOutput(true);
            builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            builder.simpleDateFormat(PecadoConstants.NORM_DATETIME_PATTERN);
            builder.modules(new PecadoJavaTimeModule());
        };
    }

}
