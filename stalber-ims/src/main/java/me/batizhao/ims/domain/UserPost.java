package me.batizhao.ims.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户岗位关联 实体对象
 *
 * @author batizhao
 * @since 2021-04-22
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "用户岗位关联")
@TableName("user_post")
public class UserPost implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value="用户ID")
    private Long userId;

    /**
     * 岗位ID
     */
    @ApiModelProperty(value="岗位ID")
    private Long postId;

}