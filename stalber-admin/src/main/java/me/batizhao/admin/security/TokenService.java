package me.batizhao.admin.security;

import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.domain.PecadoUser;
import me.batizhao.common.core.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

/**
 * @author batizhao
 * @date 2021/12/17
 */
@Component
@Slf4j
public class TokenService {

    @Autowired
    private RedisUtil redisUtil;

    @Value("${pecado.jwt.expire:30}")
    private int expire;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public PecadoUser getUser(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotEmpty(header) && header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            String token = header.replace(SecurityConstants.TOKEN_PREFIX, "");
            String uid = null;
            try {
                SignedJWT signed = SignedJWT.parse(token);
                uid = signed.getJWTClaimsSet().getStringClaim(SecurityConstants.LOGIN_KEY_UID);
            } catch (ParseException e) {
                log.info("Error in parsing token：->", e);
            }
            return redisUtil.getCacheObject(SecurityConstants.CACHE_LOGIN_KEY_UID + uid);
        }
        return null;
    }

    /**
     * 刷新令牌有效期
     *
     * @param pecadoUser 登录信息
     */
    private void refreshToken(PecadoUser pecadoUser) {
        pecadoUser.setLoginTime(System.currentTimeMillis());
        pecadoUser.setExpireTime(pecadoUser.getLoginTime() + expire * 60 * 1000L);
        redisUtil.setCacheObject(SecurityConstants.CACHE_LOGIN_KEY_UID + pecadoUser.getUid(), pecadoUser, expire, TimeUnit.MINUTES);
    }

    public void verifyToken(PecadoUser pecadoUser) {
        long expireTime = pecadoUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= 15 * 60 * 1000L)
        {
            refreshToken(pecadoUser);
        }
    }

}
