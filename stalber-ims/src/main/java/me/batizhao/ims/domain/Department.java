package me.batizhao.ims.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.batizhao.common.core.domain.TreeNode;

import java.io.Serializable;

/**
 * 部门 实体对象
 *
 * @author batizhao
 * @since 2021-04-25
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "部门")
@TableName("department")
public class Department extends TreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 第三方ID
     */
    @Schema(description="第三方ID")
    private String uuid;
                                                
    /**
     * 名称
     */
    @Schema(description="名称")
    private String name;
                        
    /**
     * 全名
     */
    @Schema(description="全名")
    private String fullName;
                        
    /**
     * 排序
     */
    @Schema(description="排序")
    private Long sort;

    /**
     * 编码
     */
    @Schema(description="编码")
    private String code;

    /**
     * 状态
     */
    @Schema(description="状态")
    private String status;

    /**
     * 类型（C公司 D部门）
     */
    @Schema(description="类型（C公司 D部门）")
    private String type;

    /**
     * 级别（in内部 out外部）
     */
    @Schema(description="级别（in内部 out外部）")
    private String level;
            
    public Department(Integer id, Integer pid) {
        this.id = id;
        this.pid = pid;
    }

}
