package me.batizhao.system.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.system.domain.Log;
import me.batizhao.system.service.LogService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@Slf4j
@AllArgsConstructor
@Component
public class SystemLogListener {

    private final LogService logService;

    @EventListener
    @Async
    public void saveLog(SystemLogEvent event) {
        Log logDTO = (Log) event.getSource();
        log.info("Async invoke start: {}", logDTO);
        logDTO.setCreateTime(LocalDateTime.now());
        logService.save(logDTO);
        log.info("Async invoke end.");
    }

}
