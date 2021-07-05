package me.batizhao.oa.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author batizhao
 * @date 2021/7/5
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class CommentAndTask implements Serializable {

    private static final long serialVersionUID = 1L;

    private Comment comment;

    private Task task;

}
