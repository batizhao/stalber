package me.batizhao.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.app.domain.AppListParam;
import me.batizhao.app.mapper.AppListParamMapper;
import me.batizhao.app.service.AppListParamService;
import org.springframework.stereotype.Service;

/**
 * <p> 应用列表参数接口实现层 </p>
 *
 * @author wws
 * @since 2022-03-02 20:01
 */
@Service
public class AppListParamServiceImpl extends ServiceImpl<AppListParamMapper, AppListParam> implements AppListParamService {
}
