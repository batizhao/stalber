package me.batizhao.common.core.constant;

/**
 * 任务调度通用常量
 * 
 * @author ruoyi
 */
public class ScheduleConstants
{
    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    /** 执行目标key */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    /** 默认 */
//    public static final String MISFIRE_DEFAULT = "";

    /** 立即触发执行 */
    public static final String MISFIRE_IGNORE_MISFIRES = "ignore";

    /** 触发一次执行 */
    public static final String MISFIRE_FIRE_AND_PROCEED = "fire";

    /** 不触发立即执行 */
    public static final String MISFIRE_DO_NOTHING = "nothing";

    public enum Status
    {
        /**
         * 正常
         */
        OPEN("open"),
        /**
         * 暂停
         */
        CLOSE("close");

        private String value;

        private Status(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }
}
