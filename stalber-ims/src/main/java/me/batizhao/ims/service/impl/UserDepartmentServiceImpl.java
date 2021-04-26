package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.ims.domain.UserDepartment;
import me.batizhao.ims.mapper.UserDepartmentMapper;
import me.batizhao.ims.service.UserDepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户部门关联接口实现类
 *
 * @author batizhao
 * @since 2021-04-26
 */
@Service
public class UserDepartmentServiceImpl extends ServiceImpl<UserDepartmentMapper, UserDepartment> implements UserDepartmentService {

    @Override
    @Transactional
    public Boolean updateUserDepartments(List<UserDepartment> userDepartments) {
        this.remove(Wrappers.<UserDepartment>lambdaQuery().eq(UserDepartment::getUserId, userDepartments.get(0).getUserId()));
        return saveBatch(userDepartments);
    }
}