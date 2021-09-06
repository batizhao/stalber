package me.batizhao.oa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.oa.domain.Invoice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 手工开票
 *
 * @author batizhao
 * @since 2021-09-06
 */
@Mapper
public interface InvoiceMapper extends BaseMapper<Invoice> {

}
