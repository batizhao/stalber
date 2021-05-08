package me.batizhao.system.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 * 
 * @author ruoyi
 */
@Component("stalberTask")
@Slf4j
public class StalberTask
{
    public void multipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        log.info("执行多参方法：字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i);
    }

    public void params(String params)
    {
        log.info("执行有参方法：{}", params);
    }

    public void noParams()
    {
        log.info("执行无参方法");
    }
}
