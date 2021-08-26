package me.batizhao.ims.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @author batizhao
 * @date 2021/8/25
 */
@Data
@Accessors(chain = true)
public class LoginDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "username is not blank")
    private String username;

    /**
     * 用户密码
     */
    @NotBlank(message = "password is not blank")
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid = "";

}
