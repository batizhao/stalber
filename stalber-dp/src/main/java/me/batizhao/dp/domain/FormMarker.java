package me.batizhao.dp.domain;

import lombok.Data;

import java.util.List;

/**
 * @author batizhao
 * @date 2021/4/12
 */
@Data
public class FormMarker {

    private List<Element> list;
    private Config config = new Config();

}
