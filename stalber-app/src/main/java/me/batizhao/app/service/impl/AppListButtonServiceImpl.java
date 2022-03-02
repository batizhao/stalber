package me.batizhao.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.app.domain.AppListButton;
import me.batizhao.app.mapper.AppListButtonMapper;
import me.batizhao.app.service.AppListButtonService;
import org.springframework.stereotype.Service;

/**
 * <p> 应用列表接口实现层 </p>
 *
 * @author wws
 * @since 2022-03-02 20:01
 */
@Service
public class AppListButtonServiceImpl extends ServiceImpl<AppListButtonMapper, AppListButton> implements AppListButtonService {
}
