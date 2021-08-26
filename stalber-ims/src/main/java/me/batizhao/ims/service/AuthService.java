package me.batizhao.ims.service;

import me.batizhao.ims.domain.LoginDTO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * @author batizhao
 * @date 2021/8/19
 */
public interface AuthService {

    /**
     * 登录
     * @param username
     * @param password
     * @param code
     * @param uuid
     * @return
     */
    String login(String username, String password, String code, String uuid);

    /**
     * 获取验证码
     * @return
     * @throws IOException
     */
    Map<String, String> getCaptchaImage() throws IOException;
}
