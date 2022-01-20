package me.batizhao.ims.domain;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户部门关联 实体对象
 *
 * @author batizhao
 * @since 2021-04-26
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "用户部门关联")
@TableName("user_department")
public class UserDepartment implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 用户ID
     */
    @Schema(description="用户ID")
    private Long userId;

    /**
     * 部门ID
     */
    @Schema(description="部门ID")
    private Long departmentId;

}