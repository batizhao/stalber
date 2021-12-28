package me.batizhao.ims.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.batizhao.common.core.domain.BaseEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "用户")
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID", example = "100")
    private Long id;

    @Schema(description = "用户名", example = "zhangsan")
    @NotBlank(message = "username is not blank")
    @Size(min = 3, max = 30)
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "邮箱", example = "zhangsan@qq.com")
    @NotBlank(message = "email is not blank")
    @Email
    private String email;

    /**
     * @mock @cname
     */
    @Schema(description = "姓名", example = "张三")
    @NotBlank(message = "name is not blank")
    private String name;

    /**
     * 用户性别
     */
    @Schema(description="用户性别")
    private String sex;

    /**
     * 手机号码
     */
    @Schema(description="手机号码")
    private String mobileNumber;

    /**
     * @mock @url
     */
    @Schema(description = "用户头像", example = "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png")
    private String avatar;

    @Schema(description = "未读消息数量", example = "99")
    private Integer unreadCount;

    /**
     * 状态
     */
    @Schema(description="状态")
    private String status;

    /**
     * 第三方ID
     */
    @Schema(description="第三方ID")
    private String uuid;
}
