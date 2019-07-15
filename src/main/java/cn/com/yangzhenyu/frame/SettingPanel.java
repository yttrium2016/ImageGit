package cn.com.yangzhenyu.frame;

import cn.com.yangzhenyu.config.Config;
import cn.com.yangzhenyu.view.InputPanel;
import cn.com.yangzhenyu.view.YButton;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA
 * Created By 杨振宇
 * Date: 2019/4/26
 * Time: 14:02
 */
public class SettingPanel extends JPanel {

    private InputPanel address = null;
    private InputPanel username = null;
    private InputPanel password = null;
    private InputPanel localhost = null;

    public SettingPanel() {
        initView();
        initData();
    }

    private void initData() {
        this.address.setText(Config.getString("git.url", ""));
        this.username.setText(Config.getString("git.username", ""));
        this.password.setText(Config.getString("git.password", ""));
        this.localhost.setText(Config.getString("git.local", "/git"));
    }

    private void initView() {
        this.setBackground(Color.white);
        this.setLayout(new GridLayout(-1, 1));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        address = new InputPanel("Git地址:");
        username = new InputPanel("用户名:");
        password = new InputPanel("密码:", true);
        localhost = new InputPanel("本地路径:");

        this.add(address);
        this.add(username);
        this.add(password);
        this.add(localhost);
        this.add(new YButton("保存", e -> {
            Config.putString("git.url", address.getValue());
            Config.putString("git.username", username.getValue());
            Config.putString("git.password", password.getValue());
            Config.putString("git.local", localhost.getValue());
            JOptionPane.showMessageDialog(null, "保存成功", "", JOptionPane.INFORMATION_MESSAGE);
        }));

    }

}
