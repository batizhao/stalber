package me.batizhao.dp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取代码生成相关配置
 * 
 * @author ruoyi
 */
@Component
@ConfigurationProperties(prefix = "gen")
@PropertySource(value = { "classpath:generator.yml" })
public class GenConfig
{
    /** 作者 */
    public static String author;

    /**
     * 项目名
     */
    public static String projectKey;

    /** 生成包路径 */
    public static String packageName;

    /**
     * 模块名
     */
    public static String moduleName;

    /** 表前缀(类名不会包含表前缀) */
    public static String templates;

    public static String getAuthor()
    {
        return author;
    }

    @Value("${author}")
    public void setAuthor(String author)
    {
        GenConfig.author = author;
    }

    public static String getProjectKey() {
        return projectKey;
    }

    @Value("${projectKey}")
    public void setProjectKey(String projectKey) {
        GenConfig.projectKey = projectKey;
    }

    public static String getPackageName()
    {
        return packageName;
    }

    @Value("${packageName}")
    public void setPackageName(String packageName)
    {
        GenConfig.packageName = packageName;
    }

    public static String getModuleName() {
        return moduleName;
    }

    @Value("${moduleName}")
    public void setModuleName(String moduleName) {
        GenConfig.moduleName = moduleName;
    }

    public static String getTemplates()
    {
        return templates;
    }

    @Value("${templates}")
    public void setTemplates(String templates) {
        GenConfig.templates = templates;
    }
}
