package me.batizhao.admin.security;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.interfaces.RSAPublicKey;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    @Value("${pecado.jwt.public-key}")
    RSAPublicKey key;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests(authz -> authz
                .antMatchers("/uaa/token", "/uaa/captcha",
                        "/actuator/**",
                        "/swagger-ui/**", "/v2/api-docs/**", "/swagger-resources/**", "/webjars/**",
                        "/swagger-ui.html", "/v3/api-docs/**",
                        "/system/file/image/**").permitAll()
                .anyRequest().authenticated()
            )
//            .oauth2ResourceServer(oauth2 -> oauth2.jwt().jwtAuthenticationConverter(jwtAuthenticationConverter()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            ).addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * ??? JWT ??? scope ?????????????????? ?????? SCOPE_ ?????????
     * ????????? jwt claim ???????????????????????????
     * ?????????????????????????????????????????????????????????url?????????????????????????????????????????????jwtAuthenticationConverter()?????????????????????
     *
     * @return JwtAuthenticationConverter
     */
//    private JwtAuthenticationConverter jwtAuthenticationConverter() {
//        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        // ?????? SCOPE_ ?????????
//        authoritiesConverter.setAuthorityPrefix("");
//        // ???jwt claim ?????????????????????????????????????????? scope ??? scp ???????????????
//        authoritiesConverter.setAuthoritiesClaimName(SecurityConstants.DETAILS_AUTHORITIES);
//        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
//        return converter;
//    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    @SneakyThrows
    public AuthenticationManager authenticationManagerBean() {
        return super.authenticationManagerBean();
    }

}
