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
import java.util.List;
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
        YButton btnRename = new YButton("重命名");
        YButton btnUpload = new YButton("更新上传");

        btnBox.add(btnInit);
        btnBox.add(btnGet);
        btnBox.add(btnRename);
        btnBox.add(btnUpload);

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
                    // 初始化
                    jGit.initGitRepository();
                    // 记录下白名单
                    String path = jGit.getLocalPath();
                    List<String> names = FileUtils.getFileNames(new File(path));
                    if (names != null && names.size() > 0) {
                        String str = StringUtils.getWhileListStr(names);
                        Config.putString("file.while.list", str);
                    } else {
                        Config.putString("file.while.list", "");
                    }

                    handler.publish(new MyMessage("初始化完成位置:" + jGit.getLocalPath()));
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
                // 远程获取
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
                // 推送到远程
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
            handler.publish(new MyMessage("开始重命名"));
            new Thread(() -> {
                String[] whileList = Config.getString("file.while.list", "").split(",");
                FileUtils.rename(new File(jGit.getLocalPath()), whileList);
                handler.publish(new MyMessage("重命名结束"));
                flag = false;
            }).start();
        });
    }

}
