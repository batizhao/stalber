package me.batizhao.system.event;

import me.batizhao.system.domain.Log;
import org.springframework.context.ApplicationEvent;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
public class SystemLogEvent extends ApplicationEvent {

    public SystemLogEvent(Log source) {
        super(source);
    }
}
