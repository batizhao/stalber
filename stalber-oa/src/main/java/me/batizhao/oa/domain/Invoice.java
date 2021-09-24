package me.batizhao.oa.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "手工开票")
@TableName("invoice")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 标题
     */
    @ApiModelProperty(value="标题")
    private String title;

    /**
     * 单位名称
     */
    @ApiModelProperty(value="单位名称")
    private String company;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value="纳税人识别号")
    private String companyNumber;

    /**
     * 发票类型
     */
    @ApiModelProperty(value="发票类型")
    private String type;

    /**
     * 邮箱
     */
    @ApiModelProperty(value="邮箱")
    private String email;

    /**
     * 手机号
     */
    @ApiModelProperty(value="手机号")
    private String mobile;

    /**
     * 开票内容
     */
    @ApiModelProperty(value="开票内容")
    private String content;

    /**
     * 发单部门
     */
    @ApiModelProperty(value="发单部门")
    private String deptName;

    /**
     * 发单人
     */
    @ApiModelProperty(value="发单人")
    private String username;

    /**
     * 联系电话
     */
    @ApiModelProperty(value="联系电话")
    private String phoneNumber;

    /**
     * 部门主任审签
     */
    @ApiModelProperty(value="部门主任审签")
    private String deptComment;

    /**
     * 财务主任审签
     */
    @ApiModelProperty(value="财务主任审签")
    private String financeComment;

    /**
     * 电子发票代码
     */
    @ApiModelProperty(value="电子发票代码")
    private String eInvoiceCode;

    /**
     * 号码
     */
    @ApiModelProperty(value="号码")
    private String eInvoiceNumber;

    /**
     * 金额
     */
    @ApiModelProperty(value="金额")
    private String eInvoiceAmount;

    /**
     * 发单日期
     */
    @ApiModelProperty(value="发单日期")
    private LocalDateTime createTime;

    /**
     * 流程属性
     */
    @ApiModelProperty(value="流程属性")
    private transient Task task;

}
