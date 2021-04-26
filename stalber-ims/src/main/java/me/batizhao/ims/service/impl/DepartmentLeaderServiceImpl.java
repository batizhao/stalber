package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.ims.domain.DepartmentLeader;
import me.batizhao.ims.mapper.DepartmentLeaderMapper;
import me.batizhao.ims.service.DepartmentLeaderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门领导关联接口实现类
 *
 * @author batizhao
 * @since 2021-04-26
 */
@Service
public class DepartmentLeaderServiceImpl extends ServiceImpl<DepartmentLeaderMapper, DepartmentLeader> implements DepartmentLeaderService {

    @Override
    @Transactional
    public Boolean updateDepartmentLeaders(List<DepartmentLeader> departmentLeaders) {
        this.remove(Wrappers.<DepartmentLeader>lambdaQuery().eq(DepartmentLeader::getDepartmentId, departmentLeaders.get(0).getDepartmentId()));
        return saveBatch(departmentLeaders);
    }
}