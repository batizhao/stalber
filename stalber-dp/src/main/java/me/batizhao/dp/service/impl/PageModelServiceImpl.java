package me.batizhao.dp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.dp.domain.PageModel;
import me.batizhao.dp.mapper.PageModelMapper;
import me.batizhao.dp.service.PageModelService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p> 页面模型接口实现类 </p>
 *
 * @author wws
 * @since 2022-02-18 13:05
 */
@Service
public class PageModelServiceImpl extends ServiceImpl<PageModelMapper, PageModel> implements PageModelService {

    /**
     * 创建分页模型查询表达式
     * @param pageModel 页面模型查询参数
     * @return LambdaQueryWrapper<PageModel>
     */
    private LambdaQueryWrapper<PageModel> createPageModelLambda(PageModel pageModel){
        LambdaQueryWrapper<PageModel> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(pageModel.getCode())) {
            wrapper.like(PageModel::getCode, pageModel.getCode());
        }

        if (StringUtils.isNotBlank(pageModel.getName())) {
            wrapper.like(PageModel::getName, pageModel.getName());
        }

        if (StringUtils.isNotBlank(pageModel.getType())) {
            wrapper.eq(PageModel::getType, pageModel.getType());
        }

        if(StringUtils.isNotBlank(pageModel.getStatus())){
            wrapper.eq(PageModel::getStatus, pageModel.getStatus());
        }
        return wrapper;
    }

    @Override
    public IPage<PageModel> findPageModelTables(Page<PageModel> page, PageModel pageModel) {
        LambdaQueryWrapper<PageModel> wrapper  = createPageModelLambda(pageModel);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public List<PageModel> findPageModelTable(PageModel pageModel) {
        return baseMapper.selectList(createPageModelLambda(pageModel));
    }

    @Override
    public PageModel saveOrUpdatePageModelTable(PageModel pageModel) {
        if (pageModel.getId() == null) {
            pageModel.setCreateTime(LocalDateTime.now());
            pageModel.setUpdateTime(LocalDateTime.now());
            baseMapper.insert(pageModel);
        } else {
            pageModel.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(pageModel);
        }
        return pageModel;
    }

    @Override
    public Boolean updateStatus(PageModel pageModel) {
        LambdaUpdateWrapper<PageModel> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(PageModel::getId, pageModel.getId()).set(PageModel::getStatus, pageModel.getStatus());
        return baseMapper.update(null, wrapper) == 1;
    }

    @Override
    public PageModel getByPageModel(PageModel pageModel) {
        List<PageModel> list = baseMapper.selectList(createPageModelLambda(pageModel));
        if(list != null && list.size() > 0){
            return list.get(0);
        }else{
            throw new NotFoundException("未查询到符合条件的页面模板：" + pageModel.getType());
        }
    }
}
