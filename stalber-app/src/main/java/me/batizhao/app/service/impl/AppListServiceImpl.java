package me.batizhao.app.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.app.domain.AppList;
import me.batizhao.app.mapper.AppListMapper;
import me.batizhao.app.service.AppListService;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.common.core.exception.StalberException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p> 应用列表接口实现层 </p>
 *
 * @author wws
 * @since 2022-03-02 20:01
 */
@Service
public class AppListServiceImpl extends ServiceImpl<AppListMapper, AppList> implements AppListService {

    /**
     * 创建查询表达式
     * @param appList 应用流程列表参数
     * @return LambdaQueryWrapper<AppList>
     */
    private LambdaQueryWrapper<AppList> createAppListLambda(AppList appList){
        LambdaQueryWrapper<AppList> wrapper = Wrappers.lambdaQuery();
        if (appList.getAppId() != null) {
            wrapper.like(AppList::getAppId, appList.getAppId());
        }

        if (StringUtils.isNotBlank(appList.getName())) {
            wrapper.like(AppList::getName, appList.getName());
        }

        if (StringUtils.isNotBlank(appList.getCode())) {
            wrapper.like(AppList::getCode, appList.getCode());
        }

        if(StringUtils.isNotBlank(appList.getStatus())){
            wrapper.eq(AppList::getStatus, appList.getStatus());
        }
        return wrapper;
    }
    
    @Override
    public IPage<AppList> findAppList(Page<AppList> page, AppList appList) {
        LambdaQueryWrapper<AppList> wrapper  = createAppListLambda(appList);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public List<AppList> findAppList(AppList appList) {
        LambdaQueryWrapper<AppList> wrapper  = createAppListLambda(appList);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public AppList findById(Long id) {
        AppList appList = baseMapper.selectById(id);

        if(appList == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return appList;
    }

    @Override
    public AppList saveOrUpdateAppList(AppList appList) {
        if (appList.getId() == null) {
            appList.setCreateTime(LocalDateTime.now());
            appList.setUpdateTime(LocalDateTime.now());
            baseMapper.insert(appList);
        } else {
            appList.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(appList);
        }
        return appList;
    }

    @Override
    public Boolean updateStatus(AppList appList) {
        List<AppList> list = findAppList(appList);
        if(CollectionUtil.isEmpty(list)){
            LambdaUpdateWrapper<AppList> wrapper = Wrappers.lambdaUpdate();
            wrapper.eq(AppList::getId, appList.getId()).set(AppList::getStatus, appList.getStatus());
            return baseMapper.update(null, wrapper) == 1;
        }else{
            throw new StalberException("已存在激活应用列表，请先禁用已经激活的列表！！！");
        }
    }
}
