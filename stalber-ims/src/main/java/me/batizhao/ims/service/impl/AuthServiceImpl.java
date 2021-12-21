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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author batizhao
 * @date 2021/8/19
 */
@Service
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

    @Value("${pecado.jwt.expire:30}")
    private int expire;

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

        PecadoUser user = (PecadoUser) authentication.getPrincipal();

        String uid = IdUtil.fastUUID();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .claim(SecurityConstants.LOGIN_KEY_UID, uid)
                .build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256).build();
        SignedJWT jwt = new SignedJWT(header, claims);

        user.setUid(uid);
        user.setLoginTime(System.currentTimeMillis());
        user.setExpireTime(user.getLoginTime() + expire * 60 * 1000L);
        redisUtil.setCacheObject(SecurityConstants.CACHE_LOGIN_KEY_UID + uid, user, expire, TimeUnit.MINUTES);

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
