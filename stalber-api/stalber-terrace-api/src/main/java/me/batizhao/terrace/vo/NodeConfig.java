package me.batizhao.terrace.vo;

import lombok.Data;

/**
 * @author batizhao
 * @date 2021/6/23
 */
@Data
public class NodeConfig {

    private Global global;
    private Form form;
    private Button button;
    private Event event;
    private Personnel personnel;
    private Rule rule;
    private String id;

    @Data
    public static class Global {
        private boolean need;
        private boolean advice;
        private boolean sign;
        private String key;
        private boolean fullyApproved;
        private boolean circulation;
        private String controlCode;
        private boolean sendPhoneMessage;
    }

    @Data
    public static class Form {
        private boolean need;
        private String pcPath;
        private String mobilePath;
        private String controlCode;
    }

    @Data
    public static class Button {
        private boolean need;
        private String buttonAssembly;
        private String controlCode;
    }

    @Data
    public static class Event {
        private boolean need;
        private String before;
        private String after;
        private String controlCode;
    }

    @Data
    public static class Personnel {
        private boolean need;
        private String type;
        private String key;
        private String roleName;
        private String controlCode;
        private String roleCode;
    }

    @Data
    public static class Rule {
        private boolean need;
        private String ruleId;
        private String ruleName;
        private String rout;
        private String condition;
    }
}
