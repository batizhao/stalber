package me.batizhao.dp.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author batizhao
 * @date 2021/10/22
 */
@Data
public class CodeTemplateDTO {

    /**
     * 保存路径
     */
    @NotBlank
    private String path;

    /**
     * 代码内容
     */
    @NotBlank
    private String codeContent;
}
