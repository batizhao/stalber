package me.batizhao.system.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.system.domain.Dictionary;
import me.batizhao.system.mapper.DictionaryMapper;
import me.batizhao.system.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 字典接口实现类
 *
 * @author batizhao
 * @since 2021-02-07
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public IPage<Dictionary> findDictionaries(Page<Dictionary> page, Dictionary dictionary) {
        LambdaQueryWrapper<Dictionary> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(dictionary.getName())) {
            wrapper.like(Dictionary::getName, dictionary.getName());
        }
        return dictionaryMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Dictionary.DictionaryData> findByCode(String code) {
        LambdaQueryWrapper<Dictionary> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(code)) {
            wrapper.eq(Dictionary::getCode, code);
        }
        Dictionary dictionary = dictionaryMapper.selectOne(wrapper);
        JSONArray array = JSONUtil.parseArray(dictionary.getData());
        return JSONUtil.toList(array, Dictionary.DictionaryData.class);
    }

    @Override
    public Dictionary findById(Long id) {
        Dictionary dictionary = dictionaryMapper.selectById(id);

        if (dictionary == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return dictionary;
    }

    @SneakyThrows
    @Override
    @Transactional
    public Dictionary saveOrUpdateDictionary(Dictionary dictionary) {
        if (dictionary.getId() == null) {
            dictionary.setCreateTime(LocalDateTime.now());
            dictionary.setUpdateTime(LocalDateTime.now());
            dictionaryMapper.insert(dictionary);
        } else {
            dictionary.setUpdateTime(LocalDateTime.now());
            dictionaryMapper.updateById(dictionary);
        }

        return dictionary;
    }

    @Override
    @Transactional
    public Boolean updateStatus(Dictionary dictionary) {
        LambdaUpdateWrapper<Dictionary> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Dictionary::getId, dictionary.getId()).set(Dictionary::getStatus, dictionary.getStatus());
        return dictionaryMapper.update(null, wrapper) == 1;
    }
}
