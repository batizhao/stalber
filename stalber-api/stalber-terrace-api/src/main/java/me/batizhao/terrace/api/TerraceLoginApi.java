package me.batizhao.terrace.api;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import me.batizhao.terrace.dto.LoginResult;
import me.batizhao.terrace.dto.R;

/**
 * 流程平台登录接口定义
 *
 * @author batizhao
 * @date 2021/6/11
 */
public interface TerraceLoginApi {

    /**
     * 登录
     *
     * @param appCode
     * @param privateKey
     * @return
     */
    @RequestLine("POST app/login")
    @Headers("Content-Type: application/json")
    @Body("%7B\"appCode\":\"{appCode}\",\"privateKey\":\"{privateKey}\"%7D")
    R<LoginResult> login(@Param("appCode") String appCode, @Param("privateKey") String privateKey);

}
