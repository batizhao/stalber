package me.batizhao.ims.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 岗位 实体对象
 *
 * @author batizhao
 * @since 2021-04-22
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "岗位")
@TableName("post")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * pk
     */
    @Schema(description="id")
    private Long id;
        
    /**
     * 名称
     */
    @Schema(description="名称")
    private String name;
        
    /**
     * 编码
     */
    @Schema(description="编码")
    private String code;
        
    /**
     * 排序
     */
    @Schema(description="排序")
    private Long sort;
        
    /**
     * 状态
     */
    @Schema(description="状态")
    private String status;
        
    /**
     * 创建时间
     */
    @Schema(description="创建时间")
    private LocalDateTime createTime;
        
    /**
     * 修改时间
     */
    @Schema(description="修改时间")
    private LocalDateTime updateTime;
    
}
