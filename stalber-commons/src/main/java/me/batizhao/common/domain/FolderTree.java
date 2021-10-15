package me.batizhao.common.domain;

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

    private boolean isLeaf;

    private List<FolderTree> children;

    public FolderTree(String name) {
        children = new ArrayList<>();
        this.label = name;
    }

    public void addFile(FolderTree file) {
        this.children.add(file);
    }

}
