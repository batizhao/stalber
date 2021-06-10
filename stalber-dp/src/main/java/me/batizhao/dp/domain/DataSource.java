package me.batizhao.dp.domain;

import lombok.Data;

/**
 * FormMarker.config.dataSource
 *
 * @author batizhao
 * @date 2021/6/10
 */
@Data
public class DataSource {

    private String key = "";

    private String name = "";

    private String url = "";

    private String method = "";

    private boolean auto = true;

    private String responseFunc = "";

}
