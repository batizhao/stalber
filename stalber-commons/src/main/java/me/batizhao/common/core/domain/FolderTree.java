package me.batizhao.common.core.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author batizhao
 * @date 2021/10/14
 */
@Data
public class FolderTree {

    private String label;

    private String path;

    private boolean isLeaf;

    private List<FolderTree> children;

    public FolderTree(String path) {
        children = new ArrayList<>();
        this.label = path;
        this.path = path;
    }

    public void addFile(FolderTree file) {
        this.children.add(file);
    }

}
