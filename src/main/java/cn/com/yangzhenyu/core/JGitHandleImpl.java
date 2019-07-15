package cn.com.yangzhenyu.core;

import cn.com.yangzhenyu.config.Config;
import cn.com.yangzhenyu.utils.FileUtils;
import cn.com.yangzhenyu.utils.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA
 * Created By 杨振宇
 * Date: 2019/4/25
 * Time: 10:23
 */
public class JGitHandleImpl implements JGitHandle {

    private static Git git = null;
    private static String mPath = System.getProperty("user.dir");
    private UsernamePasswordCredentialsProvider credentialsProvider;

    public JGitHandleImpl() {
        //设置远程服务器上的用户名和密码 (可以不用)
        credentialsProvider = new
                UsernamePasswordCredentialsProvider(Config.getString("git.username", ""), Config.getString("git.password", ""));
    }

    public String getLocalPath() {
        return mPath + Config.getString("git.local", "/git");
    }

    @Override
    public Git openGit() throws RuntimeException {
        //git仓库地址
        try {
            if (git == null) git = Git.open(new File(getLocalPath() + "/.git"));
        } catch (Exception e) {
            System.out.println("本地仓库不存在" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("本地仓库不存在,获取本地仓库错误");
        }
        return git;
    }

    @Override
    public void closeGit() {
        if (git != null) {
            git.close();
            git = null;
        }
    }

    @Override
    public void initGitRepository() throws RuntimeException {
        closeGit();
        File file = new File(getLocalPath());
        FileUtils.deleteAllFilesOfDir(file);

        if (!file.mkdir()) throw new RuntimeException("没有创建文件的权限");

        //克隆代码库命令
        Git g = null;
        try {
            g = Git.cloneRepository()
                    .setURI(Config.getString("git.url", "")) //设置远程URI
                    .setBranch("master") //设置clone下来的分支
                    .setDirectory(file) //设置下载存放路径
                    .setCredentialsProvider(credentialsProvider) //设置权限验证
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new RuntimeException("下载仓库出错");
        } finally {
            if (g != null) {
                g.close();
            }
        }
    }

    @Override
    public void gitAdd(String fileName) throws RuntimeException {
        //提交代码
        try {
            openGit().add().addFilepattern(fileName).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new RuntimeException("添加文件失败");
        }
    }

    @Override
    public void gitAddAll() {
        //提交代码
        File file = new File(getLocalPath());
        for (File f : Objects.requireNonNull(file.listFiles())) {
            gitAdd(f.getName());
        }
    }

    @Override
    public void gitCommit(String msg) {
        //提交代码
        try {
            openGit().commit().setAll(true).setMessage(msg).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            System.out.println("git commit " + msg + "失败" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void gitRemove(String fileName) {
        try {
            openGit().rm().addFilepattern(fileName).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            System.out.println("git rm " + fileName + "失败" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void gitPush() {
        try {
            openGit().push().setRemote("origin").setCredentialsProvider(credentialsProvider).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            System.out.println("git push 失败" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void gitCheckout() {
        try {
            openGit().checkout().setAllPaths(true).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            System.out.println("git checkout 失败" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void gitPull() {
        try {
            openGit().pull().setRemote("origin").setCredentialsProvider(credentialsProvider).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            System.out.println("git push 失败" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void allFilePush() {
        gitCheckout();
        gitPull();
        gitAddAll();
        gitCommit("[" + StringUtils.now() + "] 数据修改提交");
        gitPush();
    }

    @Override
    public void submitPush() {
        gitAddAll();
        gitCommit("[" + StringUtils.now() + "] 数据提交上传");
        gitPush();
    }

    @Override
    public void getPull() {
        gitCheckout();
        gitPull();
    }


}
