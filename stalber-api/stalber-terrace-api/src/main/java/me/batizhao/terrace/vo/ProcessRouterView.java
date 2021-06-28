package me.batizhao.terrace.vo;

import lombok.Data;
import me.batizhao.terrace.dto.NodeDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author batizhao
 * @date 2021/6/28
 */
@Data
public class ProcessRouterView {

    /**
     * 流转线ID
     */
    public String id;

    /**
     * 流转线名称
     */
    public String name;

    /**
     * 流转线名称text（树适用）
     */
    public String text;

    /**
     * 是否为最末节点
     */
    public boolean leaf;

    /**
     * 控制码
     */
    public Map<String, Object> controlCode = new HashMap<>();

    /**
     * 前一个网关节点
     */
    public String gatewayId;

    /**
     * 网关类型，用于选中时候，排除其他互斥
     */
    public String gatewayType;

    /**
     * 无限向下递归网关数据流转线获取
     */
    public List<ProcessRouterView> nodes;

    /**
     * 流转目标节点
     */
    public NodeDTO node;

}
