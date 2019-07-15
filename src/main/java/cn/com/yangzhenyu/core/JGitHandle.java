package cn.com.yangzhenyu.core;

import org.eclipse.jgit.api.Git;

/**
 * Created with IntelliJ IDEA
 * Created By 杨振宇
 * Date: 2019/4/25
 * Time: 10:22
 */
public interface JGitHandle {

    Git openGit() throws  RuntimeException;

    void closeGit();

    void initGitRepository() throws RuntimeException;

    void gitAdd(String fileName);

    void gitAddAll();

    void gitCommit(String msg);

    void gitRemove(String fileName);

    void gitPush();

    void gitCheckout();

    void gitPull();

    void allFilePush();

    void submitPush();

    void getPull();

    String getLocalPath();
}
