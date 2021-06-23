package me.batizhao.terrace.dto;

import lombok.Data;

/**
 * 流程定义（模版）POJO类
 *
 * @author batizhao
 * @date 2021/6/23
 */
@Data
public class ProcessDefinitionDTO {


    /**
     * 流程定义id
     */
    public String id;

    /**
     * 流程分类
     */
    public String category;

    /**
     * 流程定义名称
     */
    public String name;

    /**
     * 流程定义key
     */
    public String key;

    /**
     * 版本号
     */
    public int version;

    /**
     * 部署id
     */
    public String deploymentId;

}
