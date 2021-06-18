package me.batizhao.terrace.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import feign.Body;
import feign.Param;
import feign.RequestLine;
import me.batizhao.terrace.dto.*;
import me.batizhao.terrace.vo.TodoTaskView;

/**
 * 流程平台接口定义
 *
 * @author batizhao
 * @date 2021/6/11
 */
public interface TerraceApi {

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
    R<Page<TodoTaskView>> loadTasks(AppTodoTaskDTO dto);

    /**
     * 通过流程任务id获取任务信息
     *
     * @param taskId 任务Id
     * @param type 任务类型：0 审批任务、 1 传阅任务
     * @return
     */
    @RequestLine("GET oa/task/{taskId}/{type}")
    R loadTaskDetail(@Param("taskId") String taskId, @Param("type") String type);

    /**
     * 流程提交
     *
     * @param dto
     * @return
     */
    @RequestLine("POST oa/runtime/submit")
    R submit(SubmitProcessDTO dto);

}
