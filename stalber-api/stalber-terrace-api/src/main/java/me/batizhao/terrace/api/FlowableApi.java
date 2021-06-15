package me.batizhao.terrace.api;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import me.batizhao.terrace.dto.LoginResult;
import me.batizhao.terrace.dto.R;
import me.batizhao.terrace.dto.StartProcessDTO;

/**
 * 流程平台接口定义
 *
 * @author batizhao
 * @date 2021/6/11
 */
public interface FlowableApi {

    /**
     * 登录
     *
     * @param code
     * @param key
     * @return
     */
    @RequestLine("POST app/login?appCode={code}&privateKey={key}")
    R<LoginResult> login(@Param("code") String code, @Param("key") String key);

    /**
     * 通过流程定义 key 获取最新流程定义，并初始化流程起始环节配置
     *
     * @param key
     * @return
     */
    @RequestLine("GET oa/repository/process/definition/{key}")
    @Headers("Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjdXJyZW50VGltZU1pbGxpcyI6IjE2MjM3NTMzODIzMzMiLCJleHAiOjE2MjM3OTY1ODIsImFjY291bnQiOiJqc29hIn0.jyGcnHzw7e3UgasuLCr5Y1NVI_KgbT-8ZD9lgwvmk9w")
    R loadProcessDefinitionByKey(@Param("key") String key);

    /**
     * 流程启动
     *
     * @param dto
     * @return
     */
    @RequestLine("POST oa/runtime/start")
    @Headers({"Content-Type: application/json", "Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjdXJyZW50VGltZU1pbGxpcyI6IjE2MjM3NzIxMTIyODEiLCJleHAiOjE2MjM4MTUzMTIsImFjY291bnQiOiJqc29hIn0.w1ipUuVSEKMjJDF0xpxlxfp3zG8AlUBdGkDfcvsFDts"})
    R start(StartProcessDTO dto);

}
