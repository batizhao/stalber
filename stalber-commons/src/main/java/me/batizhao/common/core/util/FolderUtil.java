package me.batizhao.common.core.util;

import lombok.experimental.UtilityClass;
import me.batizhao.common.core.domain.FolderTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author batizhao
 * @date 2021/10/15
 */
@UtilityClass
public class FolderUtil {

    private final String FILE_SUFFIX = ".vm";

    /**
     * 取出 path 下所有的 .vm 文件
     * @param path
     * @return
     * @throws IOException
     */
    public List<Path> build(String path) throws IOException {
        return Files.find(Paths.get(path), Integer.MAX_VALUE, (p, a) -> p.toString().endsWith(FILE_SUFFIX) && a.isRegularFile())
                .collect(Collectors.toList());
    }

    /**
     * 递归取出目录下所有的目录和 .vm 文件，并构造成一棵树
     * @param parentFolder
     * @return
     */
    public List<FolderTree> build(FolderTree parentFolder) {
        File node = new File(parentFolder.getLabel());
        if (node.isDirectory()) {
            File[] subNote = node.listFiles();
            assert subNote != null;
            for (File file : subNote) {
                FolderTree folder = new FolderTree(file.getPath());
                if (file.isDirectory()) {
                    parentFolder.addFile(folder);
                    build(folder);
                } else {
                    if (!file.getName().endsWith(FILE_SUFFIX)) continue;
                    folder.setLeaf(true);
                    parentFolder.addFile(folder);
                }
            }
        }
        return parentFolder.getChildren();
    }

    /**
     * 处理路径
     * @param folderTrees
     * @return
     */
    public List<FolderTree> build(List<FolderTree> folderTrees) {
        for (FolderTree ft : folderTrees) {
            ft.setLabel(ft.getLabel().substring(ft.getLabel().lastIndexOf("/") + 1));
            if (!ft.isLeaf()) {
                build(ft.getChildren());
            }
        }
        return folderTrees;
    }
}
