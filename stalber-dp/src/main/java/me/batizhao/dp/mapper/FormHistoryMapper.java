package me.batizhao.dp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.dp.domain.FormHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 单历史记录
 *
 * @author batizhao
 * @since 2021-08-12
 */
@Mapper
public interface FormHistoryMapper extends BaseMapper<FormHistory> {

}