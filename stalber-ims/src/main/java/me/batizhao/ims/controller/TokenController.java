package me.batizhao.ims.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import me.batizhao.common.util.R;
import me.batizhao.ims.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;

/**
 * @author batizhao
 * @date 2021/8/18
 */
@Api(tags = "Token管理")
@RestController
@Validated
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @PostMapping("/uaa/token")
    public R<String> login(@ApiParam(value = "用户名", required = true) @Size(min = 3) @RequestParam String username,
                           @ApiParam(value = "密码", required = true) @Size(min = 6) @RequestParam String password,
                           @ApiParam(value = "验证码", required = true) @Size(min = 3) @RequestParam String code) {
        return R.ok(tokenService.login(username, password, code));
    }

}
