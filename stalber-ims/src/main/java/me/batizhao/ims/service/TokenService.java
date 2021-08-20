package me.batizhao.ims.service;

/**
 * @author batizhao
 * @date 2021/8/19
 */
public interface TokenService {

    /**
     * 登录
     * @param username
     * @param password
     * @param code
     * @return
     */
    String login(String username, String password, String code);
}
