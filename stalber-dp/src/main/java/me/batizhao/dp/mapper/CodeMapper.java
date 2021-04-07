package me.batizhao.dp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.dp.domain.Code;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author batizhao
 * @date 2020/10/10
 */
@Mapper
public interface CodeMapper extends BaseMapper<Code> {

    /**
     * 根据数据源查询表
     * @param page
     * @return
     */
    IPage<Code> selectTablePageByDs(Page<Code> page, @Param("code") Code code);

}
