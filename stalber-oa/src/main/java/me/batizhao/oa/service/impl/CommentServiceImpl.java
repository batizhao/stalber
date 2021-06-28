package me.batizhao.oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.exception.NotFoundException;
import me.batizhao.oa.domain.Comment;
import me.batizhao.oa.mapper.CommentMapper;
import me.batizhao.oa.service.CommentService;
import me.batizhao.terrace.api.TerraceApi;
import me.batizhao.terrace.dto.ApplicationDTO;
import me.batizhao.terrace.dto.CandidateDTO;
import me.batizhao.terrace.dto.ProcessNodeDTO;
import me.batizhao.terrace.dto.StartProcessDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * 审批接口实现类
 *
 * @author batizhao
 * @since 2021-06-10
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private TerraceApi terraceApi;

    @Override
    public IPage<Comment> findComments(Page<Comment> page, Comment comment) {
        LambdaQueryWrapper<Comment> wrapper = Wrappers.lambdaQuery();
        return commentMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Comment> findComments(Comment comment) {
        LambdaQueryWrapper<Comment> wrapper = Wrappers.lambdaQuery();
        return commentMapper.selectList(wrapper);
    }

    @Override
    public Comment findById(Long id) {
        Comment comment = commentMapper.selectById(id);

        if(comment == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return comment;
    }

    @Override
    @Transactional
    public Comment saveOrUpdateComment(Comment comment) {
        if (comment.getId() == null) {
            comment.setCreateTime(LocalDateTime.now());
            comment.setUpdateTime(LocalDateTime.now());

            StartProcessDTO dto = new StartProcessDTO();
            dto.setProcessDefinitionId("jsoa_njfw:1:1292510");
            dto.setCurrent("usertask1");
            dto.setUserId("1");
            dto.setUserName("admin");
            dto.setTenantId("23");
            dto.setOrgId("1");
            dto.setOrgName("jiangsu");
            dto.setDraft(false);

            ProcessNodeDTO processNodeDTO = new ProcessNodeDTO();
            processNodeDTO.setTarget("usertask2");
            processNodeDTO.setFlowName("南京发文流程");

            CandidateDTO candidateDTO = new CandidateDTO();
            candidateDTO.setUserId("1");
            candidateDTO.setOrgId("2");
            processNodeDTO.setCandidate(asList(candidateDTO));

            List<ProcessNodeDTO> processNodeDTOList = asList(processNodeDTO);
            dto.setProcessNodeDTO(processNodeDTOList);

            ApplicationDTO applicationDTO = new ApplicationDTO();
            applicationDTO.setId("1");
            applicationDTO.setCode("xxx");
            applicationDTO.setModuleId("12");
            applicationDTO.setModuleName("oa");
            applicationDTO.setTitle(comment.getTitle());
            applicationDTO.setCreator("admin");
            dto.setDto(applicationDTO);
            terraceApi.start(dto);

            commentMapper.insert(comment);
        } else {
            comment.setUpdateTime(LocalDateTime.now());
            commentMapper.updateById(comment);
        }

        return comment;
    }

    @Override
    @Transactional
    public Boolean updateStatus(Comment comment) {
        LambdaUpdateWrapper<Comment> wrapper = Wrappers.lambdaUpdate();
//        wrapper.eq(Comment::getId, comment.getId()).set(Comment::getStatus, comment.getStatus());
        return commentMapper.update(null, wrapper) == 1;
    }
}