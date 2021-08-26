package me.batizhao.ims.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.google.code.kaptcha.Producer;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import me.batizhao.common.constant.PecadoConstants;
import me.batizhao.common.constant.SecurityConstants;
import me.batizhao.common.domain.PecadoUser;
import me.batizhao.common.exception.StalberException;
import me.batizhao.common.util.RedisUtil;
import me.batizhao.ims.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author batizhao
 * @date 2021/8/19
 */
@Service
@CacheConfig(cacheNames = "auth")
public class AuthServiceImpl implements AuthService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Value("${pecado.captcha.enabled}")
    private Boolean captchaEnabled;

    // 验证码类型
    @Value("${pecado.captcha.type}")
    private String captchaType;

    @Value("${pecado.jwt.private-key}")
    RSAPrivateKey key;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String login(String username, String password, String code, String uuid) {
        if (captchaEnabled) {
            validateCaptcha(code, uuid);
        }

        // 调用loadUserByUsername
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        Instant now = Instant.now();
        long expiry = 3600L;
        // @formatter:off
        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        PecadoUser user = (PecadoUser) authentication.getPrincipal();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issueTime(new Date(now.toEpochMilli()))
                .expirationTime(new Date(now.plusSeconds(expiry).toEpochMilli()))
                .claim(SecurityConstants.DETAILS_USER_ID, user.getUserId())
                .claim(SecurityConstants.DETAILS_USERNAME, user.getUsername())
                .claim(SecurityConstants.DETAILS_DEPT_ID, user.getDeptIds())
                .claim(SecurityConstants.DETAILS_ROLE_ID, user.getRoleIds())
                .claim(SecurityConstants.DETAILS_AUTHORITIES, authorities)
                .build();
        // @formatter:on
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256).build();
        SignedJWT jwt = new SignedJWT(header, claims);

        return sign(jwt).serialize();
    }

    SignedJWT sign(SignedJWT jwt) {
        try {
            jwt.sign(new RSASSASigner(this.key));
            return jwt;
        }
        catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    public Map<String, String> getCaptchaImage() throws IOException {
        String capStr, code = null;
        BufferedImage image = null;

        // 生成验证码
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }
        else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        String uuid = IdUtil.simpleUUID();
        redisUtil.setCacheObject(PecadoConstants.CACHE_KEY_CAPTCHA + uuid, code, 2, TimeUnit.MINUTES);

        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        assert image != null;
        ImageIO.write(image, "jpg", os);

        Map<String, String> result = new HashMap<>(2);
        result.put("uuid", uuid);
        result.put("code", Base64.encode(os.toByteArray()));
        return result;
    }

    private void validateCaptcha(String code, String uuid) {
        String captcha = redisUtil.getCacheObject(PecadoConstants.CACHE_KEY_CAPTCHA + uuid);
        redisUtil.deleteObject(uuid);
        if (captcha == null) {
            throw new StalberException("验证码失效");
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new StalberException("验证码错误");
        }
    }
}
