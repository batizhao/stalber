package me.batizhao.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.app.domain.AppForm;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应用表单
 *
 * @author batizhao
 * @since 2022-02-24
 */
@Mapper
public interface AppFormMapper extends BaseMapper<AppForm> {

}
