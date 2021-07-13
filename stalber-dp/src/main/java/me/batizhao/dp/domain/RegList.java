package me.batizhao.dp.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author batizhao
 * @date 2021/7/9
 */
@Data
@Accessors(chain = true)
public class RegList {

    private String pattern;

    private String message;

}
