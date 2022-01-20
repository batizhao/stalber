package me.batizhao.ims.unit.service;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.ims.domain.Department;
import me.batizhao.ims.mapper.DepartmentMapper;
import me.batizhao.ims.service.DepartmentLeaderService;
import me.batizhao.ims.service.DepartmentRelationService;
import me.batizhao.ims.service.DepartmentService;
import me.batizhao.ims.service.UserDepartmentService;
import me.batizhao.ims.service.impl.DepartmentServiceImpl;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * 部门
 *
 * @author batizhao
 * @since 2021-04-25
 */
public class DepartmentServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public DepartmentService departmentService() {
            return new DepartmentServiceImpl();
        }
    }

    @MockBean
    private DepartmentMapper departmentMapper;
    @MockBean
    private UserDepartmentService UserDepartmentService;
    @MockBean
    private DepartmentRelationService departmentRelationService;
    @MockBean
    private DepartmentLeaderService departmentLeaderService;

    @Autowired
    private DepartmentService departmentService;

    private List<Department> departmentList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        departmentList = new ArrayList<>();
        departmentList.add(new Department(1, 0).setName("zhangsan"));
        departmentList.add(new Department(2, 1).setName("lisi"));
        departmentList.add(new Department(3, 2).setName("wangwu"));
    }

    @Test
    public void givenDepartmentId_whenFindDepartment_thenSuccess() {
        when(departmentMapper.selectById(1L))
                .thenReturn(departmentList.get(0));

        Department department = departmentService.findById(1L);

        assertThat(department.getName(), equalTo("zhangsan"));
    }

    @Test
    public void givenDepartmentId_whenFindDepartment_thenNotFound() {
        when(departmentMapper.selectById(any()))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> departmentService.findById(1L));

        verify(departmentMapper).selectById(any());
    }

    @Test
    public void givenDepartmentJson_whenSaveOrUpdateDepartment_thenSuccess() {
        Department department_test_data = new Department().setName("zhaoliu");

        // insert 不带 id
        doReturn(1).when(departmentMapper).insert(any(Department.class));

        departmentService.saveOrUpdateDepartment(department_test_data);

        verify(departmentMapper).insert(any(Department.class));

        // update 需要带 id
        doReturn(1).when(departmentMapper).updateById(any(Department.class));

        departmentService.saveOrUpdateDepartment(departmentList.get(0));

        verify(departmentMapper).updateById(any(Department.class));
    }

    @Test
    public void givenDepartment_whenUpdateStatus_thenSuccess() {
        //Fix can not find lambda cache for this entity
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Department.class);

        doReturn(1).when(departmentMapper).update(any(), any(Wrapper.class));
        assertThat(departmentService.updateStatus(departmentList.get(0)), equalTo(true));

        doReturn(0).when(departmentMapper).update(any(), any(Wrapper.class));
        assertThat(departmentService.updateStatus(departmentList.get(0)), equalTo(false));

        verify(departmentMapper, times(2)).update(any(), any(LambdaUpdateWrapper.class));
    }
}