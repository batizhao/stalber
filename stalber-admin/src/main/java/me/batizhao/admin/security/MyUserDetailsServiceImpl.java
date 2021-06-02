package me.batizhao.admin.security;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.domain.PecadoUser;
import me.batizhao.ims.domain.User;
import me.batizhao.ims.domain.UserInfoVO;
import me.batizhao.ims.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author batizhao
 * @since 2020-02-29
 */
@Component
@Slf4j
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("Record not found '%s'。", username));
        }

        UserInfoVO userInfoVO = userService.getUserInfo(user.getId());
        log.info("UserDetails: {}", userInfoVO);

        Set<String> authSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(userInfoVO.getRoles())) {
            // 获取角色
            authSet.addAll(userInfoVO.getRoles());
            // 获取资源
            authSet.addAll(userInfoVO.getPermissions());
        }

        Collection<? extends GrantedAuthority> authorities = AuthorityUtils
                .createAuthorityList(authSet.toArray(new String[0]));

        return new PecadoUser(user.getId(), userInfoVO.getDeptIds(), userInfoVO.getRoleIds(), user.getUsername(), user.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }
}
