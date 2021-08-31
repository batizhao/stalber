package me.batizhao.terrace.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import feign.Param;
import feign.RequestLine;
import me.batizhao.terrace.dto.AppTodoTaskDTO;
import me.batizhao.terrace.dto.R;
import me.batizhao.terrace.dto.StartProcessDTO;
import me.batizhao.terrace.dto.SubmitProcessDTO;
import me.batizhao.terrace.vo.InitProcessDefView;
import me.batizhao.terrace.vo.ProcessRouterView;
import me.batizhao.terrace.vo.TaskNodeView;
import me.batizhao.terrace.vo.TodoTaskView;

import java.util.List;

/**
 * 流程平台接口定义
 *
 * @author batizhao
 * @date 2021/6/11
 */
public interface TerraceApi {

    /**
     * 通过流程定义 key 获取最新流程定义，并初始化流程起始环节配置
     *
     * @param key
     * @return
     */
    @RequestLine("GET oa/repository/process/definition/{key}")
    R<InitProcessDefView> loadProcessDefinitionByKey(@Param("key") String key);

    /**
     * 流程启动
     *
     * @param dto
     * @return
     */
    @RequestLine("POST oa/runtime/start")
    R<String> start(StartProcessDTO dto);

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
    R<TaskNodeView> loadTaskDetail(@Param("taskId") String taskId, @Param("type") String type);

    /**
     * 获取环节的输出路由及路由后的任务环节配置信息
     *
     * @param id 流程定义Id
     * @param taskDefKey 流程环节key
     * @return
     */
    @RequestLine("GET /oa/repository/{id}/{taskDefKey}/next/rout")
    R<List<ProcessRouterView>> loadProcessRouter(@Param("taskDefKey") String taskDefKey, @Param("id") String id);

    /**
     * 流程提交
     *
     * @param dto
     * @return
     */
    @RequestLine("POST oa/runtime/submit")
    R<String> submit(SubmitProcessDTO dto);

}
