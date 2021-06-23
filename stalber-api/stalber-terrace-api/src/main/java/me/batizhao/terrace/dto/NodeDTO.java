package me.batizhao.terrace.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 流程节点POJO类
 *
 * @author batizhao
 * @date 2021/6/23
 */
@Data
public class NodeDTO {

    /**
     * 流程节点ID
     */
    public String id;

    /**
     * 流程节点名称
     */
    public String name;

    /**
     * 节点类型(0、审批，1、并行会签 2、顺序会签 3、征求部内意见（扩展） 4、送阅（扩展）)，后续
     * 还可以继续进行复杂扩展，比如子流程办结、
     */
    public String type;

    /**
     * 封装流程候选人变量key
     */
    public List<String> userKeys = new ArrayList<>();

    /**
     * 封装流程候选组变量key
     */
    public List<String> groupKeys = new ArrayList<>();

    /**
     * 控制自定义码
     */
    public Map<String, Object> controlCode;
}
