package me.batizhao.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.annotation.SystemLog;
import me.batizhao.common.core.util.R;
import me.batizhao.system.domain.Dictionary;
import me.batizhao.system.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 字典 API
 *
 * @module system
 *
 * @author batizhao
 * @since 2021-02-07
 */
@Tag(name = "字典管理")
@RestController
@Slf4j
@Validated
@RequestMapping("system")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param dictionary 字典
     * @return R
     * @real_return R<Page<Dictionary>>
     */
    @Operation(description = "分页查询字典")
    @GetMapping("/dictionaries")
    @PreAuthorize("@pms.hasPermission('system:dict:admin')")
    @SystemLog
    public R<IPage<Dictionary>> handleDictionaries(Page<Dictionary> page, Dictionary dictionary) {
        return R.ok(dictionaryService.findDictionaries(page, dictionary));
    }

    /**
     * 查询所有
     * @return R
     */
    @Operation(description = "查询所有字典")
    @GetMapping("/dictionary")
    @SystemLog
    public R<List<Dictionary>> handleDictionaries() {
        return R.ok(dictionaryService.list());
    }

//    /**
//     * 通过id查询字典
//     * @param id id
//     * @return R
//     */
//    @Operation(description = "通过id查询字典")
//    @GetMapping("/dictionary/{id}")
//    public R<Dictionary> handleId(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
//        return R.ok(dictionaryService.findById(id));
//    }

    /**
     * 通过id查询字典
     * @param code 标识
     * @return R
     */
    @Operation(description = "通过code查询字典")
    @GetMapping("/dictionary/{code}")
    public R<List<Dictionary.DictionaryData>> handleCode(@Parameter(name = "code" , required = true) @PathVariable("code") String code) {
        return R.ok(dictionaryService.findByCode(code));
    }

    /**
     * 添加或修改字典
     * @param dictionary 字典
     * @return R
     */
    @Operation(description = "添加或修改字典")
    @PostMapping("/dictionary")
    @PreAuthorize("@pms.hasPermission('system:dict:add') or @pms.hasPermission('system:dict:edit')")
    @SystemLog
    public R<Dictionary> handleSaveOrUpdate(@Valid @Parameter(name = "字典" , required = true) @RequestBody Dictionary dictionary) {
        return R.ok(dictionaryService.saveOrUpdateDictionary(dictionary));
    }

    /**
     * 通过id删除字典
     * @param ids ID串
     * @return R
     */
    @Operation(description = "通过id删除字典")
    @DeleteMapping("/dictionary")
    @PreAuthorize("@pms.hasPermission('system:dict:delete')")
    @SystemLog
    public R<Boolean> handleDelete(@Parameter(name = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(dictionaryService.removeByIds(ids));
    }

    /**
     * 更新字典状态
     *
     * @param dictionary 字典
     * @return R
     */
    @Operation(description = "更新字典状态")
    @PostMapping("/dictionary/status")
    @PreAuthorize("@pms.hasPermission('system:dict:admin')")
    @SystemLog
    public R<Boolean> handleUpdateStatus(@Parameter(name = "字典" , required = true) @RequestBody Dictionary dictionary) {
        return R.ok(dictionaryService.updateStatus(dictionary));
    }

}
