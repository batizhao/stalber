package me.batizhao.oa.domain;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 手工开票 实体对象
 *
 * @author batizhao
 * @since 2021-09-06
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "手工开票")
@TableName("invoice")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @Schema(description="id")
    private Long id;

    /**
     * 标题
     */
    @Schema(description="标题")
    private String title;

    /**
     * 单位名称
     */
    @Schema(description="单位名称")
    private String company;

    /**
     * 纳税人识别号
     */
    @Schema(description="纳税人识别号")
    private String companyNumber;

    /**
     * 发票类型
     */
    @Schema(description="发票类型")
    private String type;

    /**
     * 邮箱
     */
    @Schema(description="邮箱")
    private String email;

    /**
     * 手机号
     */
    @Schema(description="手机号")
    private String mobile;

    /**
     * 开票内容
     */
    @Schema(description="开票内容")
    private String content;

    /**
     * 发单部门
     */
    @Schema(description="发单部门")
    private String deptName;

    /**
     * 发单人
     */
    @Schema(description="发单人")
    private String username;

    /**
     * 联系电话
     */
    @Schema(description="联系电话")
    private String phoneNumber;

    /**
     * 部门主任审签
     */
    @Schema(description="部门主任审签")
    private String deptComment;

    /**
     * 财务主任审签
     */
    @Schema(description="财务主任审签")
    private String financeComment;

    /**
     * 电子发票代码
     */
    @Schema(description="电子发票代码")
    private String eInvoiceCode;

    /**
     * 号码
     */
    @Schema(description="号码")
    private String eInvoiceNumber;

    /**
     * 金额
     */
    @Schema(description="金额")
    private String eInvoiceAmount;

    /**
     * 发单日期
     */
    @Schema(description="发单日期")
    private LocalDateTime createTime;

    /**
     * 流程属性
     */
    @Schema(description="流程属性")
    private transient Task task;

}
