package me.batizhao.common.domain;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * 实体对象基类
 *
 * @author batizhao
 * @since 2021-04-25
 */
public class BaseEntity {

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    /**
     * 数据权限
     */
    @ApiModelProperty(value = "数据权限")
    private transient String dataPermission;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getDataPermission() {
        return dataPermission;
    }

    public void setDataPermission(String dataPermission) {
        this.dataPermission = dataPermission;
    }
}
