package me.batizhao.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.annotation.SystemLog;
import me.batizhao.common.util.R;
import me.batizhao.system.domain.DictData;
import me.batizhao.system.service.DictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 字典 API
 *
 * @module system
 *
 * @author batizhao
 * @since 2021-02-08
 */
@Api(tags = "字典管理")
@RestController
@Slf4j
@Validated
public class DictDataController {

    @Autowired
    private DictDataService dictDataService;

    /**
     * 通过id查询字典
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询字典")
    @GetMapping("/system/dict/data/{id}")
    public R<DictData> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(dictDataService.findById(id));
    }

    /**
     * 通过code查询字典
     * @param code code
     * @return R
     */
    @ApiOperation(value = "通过code查询字典")
    @GetMapping(value = "/system/dict/data", params = "code")
    public R<List<DictData>> handleCode(@ApiParam(value = "code", required = true) @RequestParam @Size(min = 1) String code) {
        return R.ok(dictDataService.list(Wrappers.<DictData>lambdaQuery().eq(DictData::getCode, code)));
    }

    /**
     * 添加或编辑字典
     * @param dictData 字典
     * @return R
     */
    @ApiOperation(value = "添加或编辑字典")
    @PostMapping("/system/dict/data")
    @PreAuthorize("@pms.hasPermission('system:dict:add') or @pms.hasPermission('system:dict:edit')")
    @SystemLog
    public R<DictData> handleSaveOrUpdate(@Valid @ApiParam(value = "字典" , required = true) @RequestBody DictData dictData) {
        return R.ok(dictDataService.saveOrUpdateDictData(dictData));
    }

    /**
     * 通过id删除字典
     * @param ids ID串
     * @return R
     */
    @ApiOperation(value = "通过id删除字典")
    @DeleteMapping("/system/dict/data")
    @PreAuthorize("@pms.hasPermission('system:dict:delete')")
    @SystemLog
    public R<Boolean> handleDelete(@ApiParam(value = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(dictDataService.removeByIds(ids));
    }

    /**
     * 更新字典状态
     *
     * @param dictData 字典
     * @return R
     */
    @ApiOperation(value = "更新字典状态")
    @PostMapping("/system/dict/data/status")
    @PreAuthorize("@pms.hasPermission('system:dict:admin')")
    @SystemLog
    public R<Boolean> handleUpdateStatus(@ApiParam(value = "字典" , required = true) @RequestBody DictData dictData) {
        return R.ok(dictDataService.updateStatus(dictData));
    }

}
