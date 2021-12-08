package me.batizhao.dp.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 数据源
 *
 * @author batizhao
 * @since 2020-10-19
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "数据源")
public class Ds extends Model<Ds> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @Schema(description="id", example = "100")
    private Integer id;

    /**
     * 名称
     */
    @Schema(description="名称")
    private String name;

    /**
     * url
     */
    @Schema(description="url")
    private String url;

    /**
     * 用户名
     */
    @Schema(description="用户名")
    private String username;

    /**
     * 密码
     */
    @Schema(description="密码")
    private String password;

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
