package me.batizhao.dp.domain;

import lombok.Data;

/**
 * @author batizhao
 * @date 2021/4/12
 */
@Data
public class Config {

    private int labelWidth = 100;
    private String labelPosition = "right";
    private String size = "small";
    private String customClass;
    private String ui = "element";
    private String layout = "horizontal";
    private int labelCol = 3;
    private String width = "100%";
    private boolean hideLabel = false;
    private boolean hideErrorMessage = false;

}
