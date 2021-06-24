package me.batizhao.terrace.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

/**
 * @author batizhao
 * @date 2021/6/23
 */
@Data
public class TaskNodeView {

    private Dto dto;
    private Config config;
    private List<CheckUserList> checkUserList;

    @Data
    public static class Dto {
        private String id;
        private String name;
        private String type;
        private List<String> userKeys;
        private List<String> groupKeys;
        private String controlCode;
    }

    @Data
    public static class Config {
        private String id;
        private String processDefId;
        private String taskDefKey;
        private String businessId;
        @JsonDeserialize(using = ProcessNodeConfig.NodeConfigDeserializer.class)
        private NodeConfig config;
    }

    @Data
    public static class CheckUserList {
        private String id;
        private String taskId;
        private String procInstId;
        private String taskDefKey;
        private String userId;
        private String userName;
        private String orgId;
        private String orgName;
        private String roleId;
        private String roleName;
        private String principal;
        private String principalName;
        private long createTime;
    }
}
