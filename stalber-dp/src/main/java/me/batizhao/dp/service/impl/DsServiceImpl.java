package me.batizhao.dp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.exception.DataSourceException;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.common.core.util.SpringContextHolder;
import me.batizhao.datasource.DataSourceConstants;
import me.batizhao.dp.domain.Ds;
import me.batizhao.dp.mapper.DsMapper;
import me.batizhao.dp.service.DsService;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * 数据源
 *
 * @author batizhao
 * @since 2020-10-19
 */
@Service
@Slf4j
public class DsServiceImpl extends ServiceImpl<DsMapper, Ds> implements DsService {

    @Autowired
    private DsMapper dsMapper;

    @Autowired
    private StringEncryptor stringEncryptor;

    @Autowired
    private DataSourceCreator hikariDataSourceCreator;

    @Override
    public IPage<Ds> findDss(Page<Ds> page, Ds ds) {
        LambdaQueryWrapper<Ds> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(ds.getName())) {
            wrapper.like(Ds::getName, ds.getName());
        }
        if (StringUtils.isNotBlank(ds.getUsername())) {
            wrapper.like(Ds::getUsername, ds.getUsername());
        }

        return dsMapper.selectPage(page, wrapper);
    }

    @Override
    public Ds findById(Integer id) {
        Ds ds = dsMapper.selectById(id);

        if(ds == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return ds;
    }

    @Override
    @Transactional
    public Ds saveOrUpdateDs(Ds ds) {
        checkDataSource(ds);

        if (ds.getId() == null) {
            // 添加动态数据源
            addDynamicDataSource(ds);

            ds.setCreateTime(LocalDateTime.now());
            ds.setUpdateTime(LocalDateTime.now());
            ds.setPassword(stringEncryptor.encrypt(ds.getPassword()));
            dsMapper.insert(ds);
        } else {
            // 先移除
            SpringContextHolder.getBean(DynamicRoutingDataSource.class)
                    .removeDataSource(findById(ds.getId()).getName());

            // 再添加
            addDynamicDataSource(ds);

            ds.setUpdateTime(LocalDateTime.now());
            if (StrUtil.isNotBlank(ds.getPassword())) {
                ds.setPassword(stringEncryptor.encrypt(ds.getPassword()));
            }
            dsMapper.updateById(ds);
        }

        return ds;
    }

    public void addDynamicDataSource(Ds ds) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setPoolName(ds.getName());
        dataSourceProperty.setUrl(ds.getUrl());
        dataSourceProperty.setUsername(ds.getUsername());
        dataSourceProperty.setPassword(ds.getPassword());
        dataSourceProperty.setDriverClassName(DataSourceConstants.DS_DRIVER);
        dataSourceProperty.setLazy(true);
        DataSource dataSource = hikariDataSourceCreator.createDataSource(dataSourceProperty);
        SpringContextHolder.getBean(DynamicRoutingDataSource.class).addDataSource(dataSourceProperty.getPoolName(),
                dataSource);
    }

    public Boolean checkDataSource(Ds ds) {
        try {
            DriverManager.getConnection(ds.getUrl(), ds.getUsername(), ds.getPassword());
        }
        catch (SQLException e) {
            log.error("Data source configuration {} , Failed to get connection", ds.getName(), e);
            throw new DataSourceException("Failed to get connection", e);
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean updateStatus(Ds ds) {
        LambdaUpdateWrapper<Ds> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Ds::getId, ds.getId()).set(Ds::getStatus, ds.getStatus());
        return dsMapper.update(null, wrapper) == 1;
    }
}
