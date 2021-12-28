package me.batizhao.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.system.domain.DictData;
import me.batizhao.system.mapper.DictDataMapper;
import me.batizhao.system.service.DictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 字典接口实现类
 *
 * @author batizhao
 * @since 2021-02-08
 */
@Service
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {

    @Autowired
    private DictDataMapper dictDataMapper;

    @Override
    public DictData findById(Long id) {
        DictData dictData = dictDataMapper.selectById(id);

        if(dictData == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return dictData;
    }

    @Override
    @Transactional
    public DictData saveOrUpdateDictData(DictData dictData) {
        if (dictData.getId() == null) {
            dictData.setCreateTime(LocalDateTime.now());
            dictData.setUpdateTime(LocalDateTime.now());
            dictDataMapper.insert(dictData);
        } else {
            dictData.setUpdateTime(LocalDateTime.now());
            dictDataMapper.updateById(dictData);
        }

        return dictData;
    }

    @Override
    @Transactional
    public Boolean updateStatus(DictData dictData) {
        LambdaUpdateWrapper<DictData> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(DictData::getId, dictData.getId()).set(DictData::getStatus, dictData.getStatus());
        return dictDataMapper.update(null, wrapper) == 1;
    }
}
