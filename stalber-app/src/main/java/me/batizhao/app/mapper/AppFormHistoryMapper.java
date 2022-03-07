
package me.batizhao.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.app.domain.AppFormHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 单历史记录
 *
 * @author batizhao
 * @since 2021-08-12
 */
@Mapper
public interface AppFormHistoryMapper extends BaseMapper<AppFormHistory> {

}