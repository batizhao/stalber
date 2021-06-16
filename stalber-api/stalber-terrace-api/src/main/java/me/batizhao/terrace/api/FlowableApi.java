package me.batizhao.terrace.api;

import feign.Body;
import feign.Param;
import feign.RequestLine;
import me.batizhao.terrace.dto.AppTodoTaskDTO;
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
     * @param appCode
     * @param privateKey
     * @return
     */
    @RequestLine("POST app/login")
    @Body("%7B\"appCode\":\"{appCode}\",\"privateKey\":\"{privateKey}\"%7D")
    R<LoginResult> login(@Param("appCode") String appCode, @Param("privateKey") String privateKey);

    /**
     * 通过流程定义 key 获取最新流程定义，并初始化流程起始环节配置
     *
     * @param key
     * @return
     */
    @RequestLine("GET oa/repository/process/definition/{key}")
    R loadProcessDefinitionByKey(@Param("key") String key);

    /**
     * 流程启动
     *
     * @param dto
     * @return
     */
    @RequestLine("POST oa/runtime/start")
    R start(StartProcessDTO dto);

    /**
     * 任务获取
     *
     * @param dto
     * @return
     */
    @RequestLine("GET oa/task/todo")
    R todo(AppTodoTaskDTO dto);

}
