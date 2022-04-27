package me.batizhao.ims.aspectj;

import me.batizhao.common.core.annotation.DataScope;
import me.batizhao.common.core.domain.BaseEntity;
import me.batizhao.common.core.domain.IdName;
import me.batizhao.common.core.domain.PecadoUser;
import me.batizhao.common.core.util.SecurityUtils;
import me.batizhao.ims.domain.Role;
import me.batizhao.ims.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据过滤处理
 *
 * @author ruoyi
 */
@Aspect
@Component
public class DataScopeAspect {

    @Autowired
    private RoleService roleService;

    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "all";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "custom";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "dept";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "sub";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "oneself";

    // 配置织入点
    @Pointcut("@annotation(me.batizhao.common.core.annotation.DataScope)")
    public void dataScopePointCut() {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable {
        clearDataScope(point);
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint) {
        // 获得注解
        DataScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null) {
            return;
        }
        PecadoUser user = SecurityUtils.getUser();
        if (null != user) {
            List<Role> roleList = roleService.findRolesByUserId(user.getUserId());
            dataScopeFilter(joinPoint, user, roleList, controllerDataScope.deptAlias(),
                    controllerDataScope.userAlias());
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param user      用户
     * @param userAlias 别名
     */
    public static void dataScopeFilter(JoinPoint joinPoint, PecadoUser user, List<Role> roleList, String deptAlias, String userAlias) {
        StringBuilder sqlString = new StringBuilder();

        for (Role role : roleList) {
            String dataScope = role.getDataScope();
            if (DATA_SCOPE_ALL.equals(dataScope)) {
                sqlString = new StringBuilder();
                break;
            } else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
                sqlString.append(String.format(
                        " OR %s.departmentId IN ( SELECT departmentId FROM role_department WHERE roleId = %d ) ", deptAlias, role.getId()));
            } else if (DATA_SCOPE_DEPT.equals(dataScope)) {
                sqlString.append(String.format(" OR %s.departmentId IN ( %s ) ", deptAlias,
                        user.getDepartmentList().stream().map(IdName::getId).map(String::valueOf).collect(Collectors.toList())));
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                sqlString.append(String.format(
                        " OR %s.departmentId IN ( SELECT d.id FROM department d LEFT JOIN department_relation dr ON d.id = dr.descendant WHERE dr.ancestor IN ( %s )) ",
                        deptAlias, user.getDepartmentList().stream().map(IdName::getId).map(String::valueOf).collect(Collectors.toList())));
            } else if (DATA_SCOPE_SELF.equals(dataScope)) {
                if (StringUtils.isNotBlank(userAlias)) {
                    sqlString.append(String.format(" OR %s.id = %d ", userAlias, user.getUserId()));
                } else {
                    // 数据权限为仅本人且没有userAlias别名不查询任何数据
                    sqlString.append(" OR 1=0 ");
                }
            }
        }

        if (StringUtils.isNotBlank(sqlString.toString())) {
            Object params = joinPoint.getArgs()[0];
            if (params instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) params;
                baseEntity.setDataPermission(" AND (" + sqlString.substring(4) + ")");
            }
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }

    /**
     * 拼接权限sql前先清空params.dataScope参数防止注入
     */
    private void clearDataScope(final JoinPoint joinPoint) {
        Object params = joinPoint.getArgs()[0];
        if (params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.setDataPermission("");
        }
    }
}
