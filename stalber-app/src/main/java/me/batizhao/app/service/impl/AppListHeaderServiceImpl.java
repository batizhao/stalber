package me.batizhao.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.app.domain.AppListHeader;
import me.batizhao.app.mapper.AppListHeaderMapper;
import me.batizhao.app.service.AppListHeaderService;
import org.springframework.stereotype.Service;

/**
 * <p> 应用列表表头接口实现层 </p>
 *
 * @author wws
 * @since 2022-03-02 20:01
 */
@Service
public class AppListHeaderServiceImpl extends ServiceImpl<AppListHeaderMapper, AppListHeader> implements AppListHeaderService {
}
