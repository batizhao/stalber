package me.batizhao.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "字典管理")
@RestController
@Slf4j
@Validated
@RequestMapping("system")
public class DictDataController {

    @Autowired
    private DictDataService dictDataService;

    /**
     * 通过id查询字典
     * @param id id
     * @return R
     */
    @Operation(description = "通过id查询字典")
    @GetMapping("/dict/data/{id}")
    public R<DictData> handleId(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(dictDataService.findById(id));
    }

    /**
     * 通过code查询字典
     * @param code code
     * @return R
     */
    @Operation(description = "通过code查询字典")
    @GetMapping(value = "/dict/data", params = "code")
    public R<List<DictData>> handleCode(@Parameter(name = "code", required = true) @RequestParam @Size(min = 1) String code) {
        return R.ok(dictDataService.list(Wrappers.<DictData>lambdaQuery().eq(DictData::getCode, code)));
    }

    /**
     * 添加或编辑字典
     * @param dictData 字典
     * @return R
     */
    @Operation(description = "添加或编辑字典")
    @PostMapping("/dict/data")
    @PreAuthorize("@pms.hasPermission('system:dict:add') or @pms.hasPermission('system:dict:edit')")
    @SystemLog
    public R<DictData> handleSaveOrUpdate(@Valid @Parameter(name = "字典" , required = true) @RequestBody DictData dictData) {
        return R.ok(dictDataService.saveOrUpdateDictData(dictData));
    }

    /**
     * 通过id删除字典
     * @param ids ID串
     * @return R
     */
    @Operation(description = "通过id删除字典")
    @DeleteMapping("/dict/data")
    @PreAuthorize("@pms.hasPermission('system:dict:delete')")
    @SystemLog
    public R<Boolean> handleDelete(@Parameter(name = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(dictDataService.removeByIds(ids));
    }

    /**
     * 更新字典状态
     *
     * @param dictData 字典
     * @return R
     */
    @Operation(description = "更新字典状态")
    @PostMapping("/dict/data/status")
    @PreAuthorize("@pms.hasPermission('system:dict:admin')")
    @SystemLog
    public R<Boolean> handleUpdateStatus(@Parameter(name = "字典" , required = true) @RequestBody DictData dictData) {
        return R.ok(dictDataService.updateStatus(dictData));
    }

}
