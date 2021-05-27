package me.batizhao.system.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.batizhao.common.annotation.SystemLog;
import me.batizhao.common.constant.PecadoConstants;
import me.batizhao.common.util.R;
import me.batizhao.system.domain.Log;
import me.batizhao.system.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.List;

/**
 * 日志管理
 * 使用 AOP 实现 API 日志记录
 *
 * @module system
 *
 * @author batizhao
 * @since 2020-03-24
 **/
@Api(tags = "日志管理")
@RestController
@Validated
@RequestMapping("system")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 分页查询日志
     * @param page 分页对象
     * @param log 日志
     * @return R
     * @real_return R<Page<Log>>
     */
    @ApiOperation(value = "分页查询日志")
    @GetMapping("/logs")
    @PreAuthorize("@pms.hasPermission('system:log:admin')")
    public R<IPage<Log>> handleLogs(Page<Log> page, Log log) {
        return R.ok(logService.findLogs(page, log));
    }

    /**
     * 通过id查询日志
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询日志")
    @GetMapping("/log/{id}")
    @PreAuthorize("@pms.hasPermission('system:log:admin')")
    public R<Log> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(logService.findById(id));
    }

    /**
     * 添加日志
     * @param log 日志
     * @return R
     */
    @ApiOperation(value = "添加日志")
    @PostMapping("/log")
    @PreAuthorize("@pms.hasPermission('system:log:admin')")
    public R<Boolean> handleSaveOrUpdate(@Valid @ApiParam(value = "日志" , required = true) @RequestBody Log log) {
        return R.ok(logService.save(log));
    }

    /**
     * 通过id删除日志
     * @param ids ID串
     * @return R
     */
    @ApiOperation(value = "通过id删除日志")
    @DeleteMapping(value = "/log", params = "ids")
    @PreAuthorize("@pms.hasPermission('system:log:delete')")
    @SystemLog
    public R<Boolean> handleDelete(@ApiParam(value = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(logService.removeByIds(ids));
    }

    /**
     * 清空日志
     * @return R
     */
    @ApiOperation(value = "清空日志")
    @DeleteMapping("/log")
    @PreAuthorize("@pms.hasPermission('system:log:clean')")
    @SystemLog
    public R<Boolean> handleDeleteAllLog() {
        return R.ok(logService.remove(null));
    }

    /**
     * 导出
     * 返回Excel流
     *
     * @param log 过滤条件
     */
    @ApiOperation(value = "导出")
    @PostMapping(value = "/log/export")
    @PreAuthorize("@pms.hasPermission('system:log:export')")
    public void handleExport(Page<Log> page, Log log, HttpServletResponse response) throws IOException {
        List<Log> logs = logService.findLogs(page, log).getRecords();

        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(logs, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                String.format("attachment; filename=%s.xlsx", PecadoConstants.BACK_END_PROJECT));

        ServletOutputStream out = response.getOutputStream();

        writer.flush(out, true);
        writer.close();
        IoUtil.close(out);
    }

}
