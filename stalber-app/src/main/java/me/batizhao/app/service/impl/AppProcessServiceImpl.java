package me.batizhao.app.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.app.domain.AppProcess;
import me.batizhao.app.mapper.AppProcessMapper;
import me.batizhao.app.service.AppProcessService;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.common.core.exception.StalberException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p> 应用流程接口实现类 </p>
 *
 * @author wws
 * @since 2022-02-28 15:04
 */
@Service
public class AppProcessServiceImpl extends ServiceImpl<AppProcessMapper, AppProcess> implements AppProcessService {

    /**
     * 创建查询表达式
     * @param appProcess 应用流程查询参数
     * @return LambdaQueryWrapper<AppProcess>
     */
    private LambdaQueryWrapper<AppProcess> createAppProcessLambda(AppProcess appProcess){
        LambdaQueryWrapper<AppProcess> wrapper = Wrappers.lambdaQuery();
        if (appProcess.getAppId() != null) {
            wrapper.like(AppProcess::getAppId, appProcess.getAppId());
        }

        if (StringUtils.isNotBlank(appProcess.getName())) {
            wrapper.like(AppProcess::getName, appProcess.getName());
        }

        if (StringUtils.isNotBlank(appProcess.getProcessKey())) {
            wrapper.like(AppProcess::getProcessKey, appProcess.getProcessKey());
        }

        if (appProcess.getVersion() != null) {
            wrapper.eq(AppProcess::getVersion, appProcess.getVersion());
        }

        if(StringUtils.isNotBlank(appProcess.getStatus())){
            wrapper.eq(AppProcess::getStatus, appProcess.getStatus());
        }
        return wrapper;
    }
    
    @Override
    public IPage<AppProcess> findAppProcess(Page<AppProcess> page, AppProcess appProcess) {
        LambdaQueryWrapper<AppProcess> wrapper  = createAppProcessLambda(appProcess);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public List<AppProcess> findAppProcess(AppProcess appProcess) {
        LambdaQueryWrapper<AppProcess> wrapper  = createAppProcessLambda(appProcess);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public AppProcess findAppProcess(Long appId, Integer version) {
        AppProcess appProcess = new AppProcess();
        appProcess.setAppId(appId);
        if(version != null){
            appProcess.setVersion(version);
        }
        appProcess.setStatus("open");
        LambdaQueryWrapper<AppProcess> wrapper  = createAppProcessLambda(appProcess);
        List<AppProcess> list = baseMapper.selectList(wrapper);
        if(CollectionUtil.isNotEmpty(list)){
            return list.get(0);
        }else{
            throw new NotFoundException(String.format("Record not found '%s'。", appId));
        }
    }

    @Override
    public AppProcess findById(Long id) {
        AppProcess appProcess = baseMapper.selectById(id);

        if(appProcess == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return appProcess;
    }

    @Override
    public AppProcess saveOrUpdateAppProcess(AppProcess appProcess) {
        if (appProcess.getId() == null) {
            appProcess.setCreateTime(LocalDateTime.now());
            appProcess.setUpdateTime(LocalDateTime.now());
            baseMapper.insert(appProcess);
        } else {
            appProcess.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(appProcess);
        }
        return appProcess;
    }

    @Override
    public Boolean updateStatus(AppProcess appProcess) {
        List<AppProcess> list = findAppProcess(appProcess);
        if(CollectionUtil.isEmpty(list)){
            LambdaUpdateWrapper<AppProcess> wrapper = Wrappers.lambdaUpdate();
            wrapper.eq(AppProcess::getId, appProcess.getId()).set(AppProcess::getStatus, appProcess.getStatus());
            return baseMapper.update(null, wrapper) == 1;
        }else{
            throw new StalberException("已存在激活流程，请先禁用已经激活的流程！！！");
        }
    }
}
