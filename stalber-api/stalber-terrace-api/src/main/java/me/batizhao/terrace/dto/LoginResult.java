package me.batizhao.terrace.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 登录返回结果
 *
 * @author batizhao
 * @date 2021/6/11
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LoginResult {

    /**
     * token
     */
    private String accessToken;

    /**
     * 过期时间
     */
    private int expireTime;

}
