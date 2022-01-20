package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.ims.domain.Department;
import me.batizhao.ims.domain.DepartmentRelation;
import me.batizhao.ims.mapper.DepartmentRelationMapper;
import me.batizhao.ims.service.DepartmentRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门依赖关系接口实现类
 *
 * @author batizhao
 * @since 2021-04-29
 */
@Service
public class DepartmentRelationServiceImpl extends ServiceImpl<DepartmentRelationMapper, DepartmentRelation> implements DepartmentRelationService {

    @Autowired
    private DepartmentRelationMapper departmentRelationMapper;

    @Override
    @Transactional
    public Boolean saveDepartmentRelation(Department department) {
        // 增加部门关系表
        List<DepartmentRelation> relationList = departmentRelationMapper.selectList(
                Wrappers.<DepartmentRelation>query().lambda().eq(DepartmentRelation::getDescendant, department.getPid()))
                .stream().map(relation -> {
                    relation.setDescendant(department.getId());
                    return relation;
                }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(relationList)) {
            this.saveBatch(relationList);
        }

        // 自己也要维护到关系表中
        DepartmentRelation own = new DepartmentRelation();
        own.setDescendant(department.getId());
        own.setAncestor(department.getId());

        return departmentRelationMapper.insert(own) == 1;
    }

    @Override
    public Boolean updateDepartmentRelation(Department department) {
        this.remove(Wrappers.<DepartmentRelation>lambdaQuery().eq(DepartmentRelation::getDescendant, department.getId()));
        return saveDepartmentRelation(department);
    }
}