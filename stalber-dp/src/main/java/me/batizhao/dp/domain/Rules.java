package me.batizhao.dp.domain;

import lombok.Data;

/**
 * @author batizhao
 * @date 2021/4/12
 */
@Data
public class Rules {

    private boolean required;
    private String message;

    public Rules() {
    }

    public Rules(boolean required, String message) {
        this.required = required;
        this.message = message;
    }
}
