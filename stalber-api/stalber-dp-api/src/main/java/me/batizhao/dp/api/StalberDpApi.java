package me.batizhao.dp.api;

import feign.Param;
import feign.RequestLine;
import me.batizhao.dp.dto.Form;
import me.batizhao.dp.dto.R;

/**
 * @author batizhao
 * @date 2021/7/8
 */
public interface StalberDpApi {

    /**
     * 通过key查询表单
     * @param key key
     * @return R
     */
    @RequestLine("GET app/form?key={key}")
    R<Form> loadFormByKey(@Param("key") String key);

}
