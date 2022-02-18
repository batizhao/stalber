package me.batizhao.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.app.domain.PageModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p> 页面模型 </p>
 *
 * @author wws
 * @since 2022-02-18 11:00
 */
@Mapper
public interface PageModelMapper extends BaseMapper<PageModel> {
}
