package me.batizhao.app.domain.fg;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author batizhao
 * @date 2021/7/12
 */
@Data
@Accessors(chain = true)
public class Autosize {

    private Integer minRows = 4;

    private Integer maxRows = 4;

}
