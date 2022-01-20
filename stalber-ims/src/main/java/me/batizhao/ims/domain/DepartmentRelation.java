package me.batizhao.ims.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 部门依赖关系 实体对象
 *
 * @author batizhao
 * @since 2021-04-29
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "部门依赖关系")
@TableName("department_relation")
public class DepartmentRelation implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 祖先
     */
    @Schema(description="祖先")
    private Integer ancestor;

    /**
     * 后代
     */
    @Schema(description="后代")
    private Integer descendant;

}