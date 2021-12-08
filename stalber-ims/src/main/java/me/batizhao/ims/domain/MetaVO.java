package me.batizhao.ims.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author batizhao
 * @date 2021/3/17
 */
@Data
@Schema(description = "VUE路由元数据")
public class MetaVO {

    /**
     * 路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 路由图标
     */
    private String icon;

    /**
     * 设置为 true，则不会被 keep-alive 缓存
     */
    private boolean noCache;

    public MetaVO(String title, String icon, boolean noCache) {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
    }
}
