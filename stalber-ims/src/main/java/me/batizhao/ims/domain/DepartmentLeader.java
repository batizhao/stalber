package me.batizhao.ims.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "部门领导关联")
@TableName("department_leader")
public class DepartmentLeader implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @ApiModelProperty(value="部门ID")
    private Long departmentId;

    /**
     * 领导ID
     */
    @ApiModelProperty(value="领导ID")
    private Long leaderUserId;

}