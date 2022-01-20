package me.batizhao.ims.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 部门领导关联 实体对象
 *
 * @author batizhao
 * @since 2021-04-26
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "部门领导关联")
@TableName("department_leader")
public class DepartmentLeader implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @Schema(description="部门ID")
    private Long departmentId;

    /**
     * 领导ID
     */
    @Schema(description="领导ID")
    private Long leaderUserId;

    /**
     * 类型（Z正 F副）
     */
    @Schema(description="类型（Z正 F副）")
    private String type;

    /**
     * 排序
     */
    @Schema(description="排序")
    private Long sort;

}