package me.batizhao.ims.controller;

import io.swagger.annotations.Api;
import me.batizhao.common.util.R;
import me.batizhao.ims.domain.LoginDTO;
import me.batizhao.ims.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

/**
 * @author batizhao
 * @date 2021/8/18
 */
@Api(tags = "认证管理")
@RestController
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/uaa/token")
    public R<String> handleLogin(@Valid @RequestBody LoginDTO loginDTO) {
        return R.ok(authService.login(loginDTO.getUsername(), loginDTO.getPassword(),
                loginDTO.getCode(), loginDTO.getUuid()));
    }

    /**
     * 生成验证码
     */
    @GetMapping("/captcha")
    public R<Map<String, String>> handleCaptcha() throws IOException {
        return R.ok(authService.getCaptchaImage());
    }

}
