package cn.com.yangzhenyu.frame;

import cn.com.yangzhenyu.config.Config;
import cn.com.yangzhenyu.core.JGitHandle;
import cn.com.yangzhenyu.core.JGitHandleImpl;
import cn.com.yangzhenyu.core.MyHandler;
import cn.com.yangzhenyu.core.MyMessage;
import cn.com.yangzhenyu.utils.FileUtils;
import cn.com.yangzhenyu.utils.StringUtils;
import cn.com.yangzhenyu.view.YButton;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Git工具的页面
 * Created with IntelliJ IDEA
 * Created By 杨振宇
 * Date: 2019/4/26
 * Time: 11:49
 */
class GitPanel extends JPanel {

    private JTextArea textBox = null;
    private JGitHandle jGit = new JGitHandleImpl();
    private String mPath = System.getProperty("user.dir");
    private boolean flag = false;

    private Handler handler = new MyHandler() {
        @Override
        public void publish(LogRecord record) {
            if (textBox != null && record.getLevel() == Level.ALL) {
                textBox.append("[" + StringUtils.now() + "] " + record.getMessage());
                textBox.append("\n");
            }
        }
    };

    GitPanel() {
        initView();
    }

    private void initView() {
        // 样式
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);

        // 标题按钮
        JPanel btnBox = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnBox.setBackground(Color.white);
        YButton btnInit = new YButton("初始化");
        YButton btnGet = new YButton("获取");
        YButton btnUpload = new YButton("更新上传");
        YButton btnRename = new YButton("重命名上传");
        btnBox.add(btnInit);
        btnBox.add(btnGet);
        btnBox.add(btnUpload);
        btnBox.add(btnRename);
        this.add(btnBox, BorderLayout.PAGE_START);

        // 文本域
        textBox = new JTextArea();
        textBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JScrollPane sp = new JScrollPane(textBox);
        sp.setBackground(Color.white);
        sp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(sp, BorderLayout.CENTER);

        // 初始化 事件绑定
        btnInit.setClick(e -> {
            if (flag) return;
            if (Config.isBlank()) {
                JOptionPane.showMessageDialog(null, "请填写Git相关设置", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            flag = true;
            handler.publish(new MyMessage("开始初始化"));
            new Thread(() -> {
                try {
                    jGit.initGitRepository();
                    handler.publish(new MyMessage("初始化完成位置:" + mPath + Config.getString("git.localhost", "\\git")));
                } catch (Exception ex) {
                    handler.publish(new MyMessage(ex.getMessage()));
                }
                flag = false;
            }).start();
        });

        // 获取拉取 事件绑定
        btnGet.setClick(e -> {
            if (flag) return;
            if (Config.isBlank()) {
                JOptionPane.showMessageDialog(null, "请填写Git相关设置", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            flag = true;
            handler.publish(new MyMessage("开始远程获取"));
            new Thread(() -> {
                jGit.getPull();
                handler.publish(new MyMessage("远程获取完成"));
                flag = false;
            }).start();
        });

        // 本地推送 事件绑定
        btnUpload.setClick(e -> {
            if (flag) return;
            if (Config.isBlank()) {
                JOptionPane.showMessageDialog(null, "请填写Git相关设置", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            flag = true;
            handler.publish(new MyMessage("开始更新上传"));
            new Thread(() -> {
                jGit.submitPush();
                handler.publish(new MyMessage("更新上传完成"));
                flag = false;
            }).start();

        });

        // 重命名 推送 会拉取一次
        btnRename.setClick(e -> {
            if (flag) return;
            if (Config.isBlank()) {
                JOptionPane.showMessageDialog(null, "请填写Git相关设置", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            flag = true;
            handler.publish(new MyMessage("开始拉取"));
            new Thread(() -> {
                jGit.getPull();
                handler.publish(new MyMessage("拉取结束"));
                handler.publish(new MyMessage("开始重命名"));
                FileUtils.remove(new File(mPath + Config.getString("git.local", "/git")));
                handler.publish(new MyMessage("重命名结束"));
                handler.publish(new MyMessage("开始上传"));
                jGit.submitPush();
                handler.publish(new MyMessage("更新上传完成"));
                flag = false;
            }).start();
        });
    }

}
