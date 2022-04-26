package me.batizhao.app.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.batizhao.app.domain.AppForm;
import me.batizhao.terrace.vo.InitProcessDefView;
import me.batizhao.terrace.vo.TaskNodeView;

import java.io.Serializable;

/**
 * <p> 应用初始化对象 </p>
 *
 * @author wws
 * @since 2022-03-15 12:15
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "应用初始化对象")
public class InitApp implements Serializable {

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

    /** 表单对象 **/
    private AppForm appForm;

    /**
     * 流程初始对象
     */
    @Schema(description="流程初始对象")
    private InitProcessDefView process;

    /**
     * 流程任务对象
     */
    @Schema(description="流程任务对象")
    private TaskNodeView task;
}
