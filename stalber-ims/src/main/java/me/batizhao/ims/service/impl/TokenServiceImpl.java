package me.batizhao.ims.service.impl;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import me.batizhao.common.constant.SecurityConstants;
import me.batizhao.common.domain.PecadoUser;
import me.batizhao.ims.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author batizhao
 * @date 2021/8/19
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Value("${jwt.private-key}")
    RSAPrivateKey key;

    @Override
    public String login(String username, String password, String code) {
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
}
