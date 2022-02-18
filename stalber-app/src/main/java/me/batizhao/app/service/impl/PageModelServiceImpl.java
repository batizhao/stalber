package me.batizhao.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.app.domain.PageModel;
import me.batizhao.app.mapper.PageModelMapper;
import me.batizhao.app.service.PageModelService;
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
}
