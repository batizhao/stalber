package me.batizhao.app.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.app.domain.App;
import me.batizhao.app.domain.AppForm;
import me.batizhao.app.domain.AppProcess;
import me.batizhao.app.service.AppFormService;
import me.batizhao.app.service.AppProcessService;
import me.batizhao.app.service.AppService;
import me.batizhao.app.view.InitApp;
import me.batizhao.common.core.domain.PecadoUser;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.common.core.util.R;
import me.batizhao.common.core.util.SecurityUtils;
import me.batizhao.terrace.api.TerraceApi;
import me.batizhao.terrace.vo.InitProcessDefView;
import me.batizhao.terrace.vo.NodeConfig;
import me.batizhao.terrace.vo.TaskNodeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 应用 API
 *
 * @module app
 *
 * @author batizhao
 * @since 2022-01-21
 */
@Tag(name = "应用管理")
@RestController
@Slf4j
@Validated
@RequestMapping("app")
public class AppController {

    @Autowired
    private AppService appService;

    @Autowired
    private TerraceApi terraceApi;

    @Autowired
    private AppFormService appFormService;

    @Autowired
    private AppProcessService appProcessService;

    /**
     * 分页查询应用
     * @param page 分页对象
     * @param app 应用
     * @return R
     * @real_return R<Page<App>>
     */
    @Operation(description = "分页查询应用")
    @GetMapping("/devs")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<IPage<App>> handleApps(Page<App> page, App app) {
        return R.ok(appService.findApps(page, app));
    }

    /**
     * 查询应用
     * @return R<List<App>>
     */
    @Operation(description = "查询应用")
    @GetMapping("/dev")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<List<App>> handleApps(App app) {
        return R.ok(appService.findApps(app));
    }

    /**
     * 通过id查询应用
     * @param id id
     * @return R
     */
    @Operation(description = "通过id查询应用")
    @GetMapping("/dev/{id}")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<App> handleId(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(appService.findById(id));
    }

    /**
     * 添加或编辑应用
     * @param app 应用
     * @return R
     */
    @Operation(description = "添加或编辑应用")
    @PostMapping("/dev")
    @PreAuthorize("@pms.hasPermission('app:dev:add') or @pms.hasPermission('app:dev:edit')")
    public R<App> handleSaveOrUpdate(@Valid @Parameter(name = "应用" , required = true) @RequestBody App app) {
        return R.ok(appService.saveOrUpdateApp(app));
    }

    /**
     * 通过id删除应用
     * @param ids ID串
     * @return R
     */
    @Operation(description = "通过id删除应用")
    @DeleteMapping("/dev")
    @PreAuthorize("@pms.hasPermission('app:dev:delete')")
    public R<Boolean> handleDelete(@Parameter(name = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(appService.removeByIds(ids));
    }

    /**
     * 更新应用状态
     *
     * @param app 应用
     * @return R
     */
    @Operation(description = "更新应用状态")
    @PostMapping("/dev/status")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<Boolean> handleUpdateStatus(@Parameter(name = "应用" , required = true) @RequestBody App app) {
        return R.ok(appService.updateStatus(app));
    }


    /**
     * 通过应用Id初始化应用表单与组件默认值
     * @param key 表单key
     * @return R
     */
    @Operation(description = "通过表单key初始化应用表单与组件默认值")
    @GetMapping("/init")
    @PreAuthorize("isAuthenticated()")
    public R<InitApp> init(@RequestParam(name = "key") String key,
                           @RequestParam(name = "taskId", required = false, defaultValue = "") String taskId,
                           @RequestParam(name = "taskType", required = false, defaultValue = "") String taskType) {

        AppForm appForm = appFormService.getOne(Wrappers.<AppForm>lambdaQuery().eq(AppForm::getFormKey, key));
        if(appForm == null){
            throw new NotFoundException("表单不存在!");
        }
        App app = appService.getById(appForm.getAppId());

        InitApp initApp = new InitApp().setCode(app.getCode()).setName(app.getName());
        initApp.setAppForm(appForm);

        if(StringUtils.isNotBlank(taskId)){
            //初始化任务
            PecadoUser user = SecurityUtils.getUser();
            terraceApi.sign(taskId, user.getUsername(),StringUtils.isBlank(taskType) ? "0" : taskType);
            TaskNodeView tasks = terraceApi.loadTaskDetail(taskId, StringUtils.isBlank(taskType) ? "0" : taskType).getData();
            //根据流程配置动态加载业务处理表单
            TaskNodeView.Config config = tasks.getConfig();
            NodeConfig nodeConfig = config.getConfig();
            NodeConfig.Form formConfig = nodeConfig.getForm();
            String pcPath = formConfig.getPcPath();
            if(StringUtils.isNotBlank(pcPath)){
                AppForm newAppForm = appFormService.getOne(Wrappers.<AppForm>lambdaQuery().eq(AppForm::getFormKey, pcPath));
                if(newAppForm != null){
                    initApp.setAppForm(newAppForm);
                }
            }
            initApp.setTask(tasks);
        }else{
            //初始流程
            AppProcess queryAppProcess = new AppProcess();
            queryAppProcess.setFormId(appForm.getId());
            queryAppProcess.setStatus("open");

            List<AppProcess> appProcessList = appProcessService.findAppProcess(queryAppProcess);
            if(CollectionUtil.isNotEmpty(appProcessList)){
                AppProcess appProcess = appProcessList.get(0);
                InitProcessDefView process = terraceApi.loadProcessDefinitionByKey(appProcess.getProcessKey()).getData();
                initApp.setProcess(process);
            }
        }

        initApp.setAppForm(appForm);

        return R.ok(initApp);
    }
}
