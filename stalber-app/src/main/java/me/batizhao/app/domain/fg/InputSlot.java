package me.batizhao.app.domain.fg;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author batizhao
 * @date 2021/7/9
 */
@Data
@Accessors(chain = true)
public class InputSlot implements Slot {

    private String prepend = "";

    private String append = "";

}
