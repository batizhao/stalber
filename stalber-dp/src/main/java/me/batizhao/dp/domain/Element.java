package me.batizhao.dp.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author batizhao
 * @date 2021/4/12
 */
@Data
public class Element {

    private String type = "input";
    private String icon;
    private Options options;
    private String name;
    private String key;
    private String model;
    private List<Rules> rules = new ArrayList<>();
}
