package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.ims.domain.RoleDepartment;
import me.batizhao.ims.mapper.RoleDepartmentMapper;
import me.batizhao.ims.service.RoleDepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色数据权限接口实现类
 *
 * @author batizhao
 * @since 2021-05-28
 */
@Service
public class RoleDepartmentServiceImpl extends ServiceImpl<RoleDepartmentMapper, RoleDepartment> implements RoleDepartmentService {

    @Override
    @Transactional
    public Boolean updateRoleDepartments(List<RoleDepartment> roleDepartments) {
        this.remove(Wrappers.<RoleDepartment>lambdaQuery().eq(RoleDepartment::getRoleId, roleDepartments.get(0).getRoleId()));
        return saveBatch(roleDepartments);
    }
}
