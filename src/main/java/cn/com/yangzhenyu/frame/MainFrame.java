package cn.com.yangzhenyu.frame;

import cn.com.yangzhenyu.view.YButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 主页面
 * Created with IntelliJ IDEA
 * Created By 杨振宇
 * Date: 2019/4/19
 * Time: 12:30
 */
public class MainFrame extends JFrame {

    private static final String appTitle = "图库工具箱1.0(杨振宇专版)";

    public MainFrame() {
        super();
        initView();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }


    private void initView() {
        // 设置Logo
        ImageIcon mImageIcon = new ImageIcon(MainFrame.class.getResource("/img/icon.png"));
        this.setIconImage(mImageIcon.getImage());

        // 设置窗体
        this.setTitle(appTitle);
        this.setBounds(200, 200, 400, 300);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(Color.white);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        // 三个按钮 三个页面
        YButton git = new YButton("Git相关");
        YButton tools = new YButton("工具");
        YButton setting = new YButton("设置");

        git.setBorder(null);
        tools.setBorder(null);
        setting.setBorder(null);

        JPanel btnBox = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnBox.setBorder(null);
        btnBox.setBackground(Color.white);

        btnBox.add(git);
        btnBox.add(tools);
        btnBox.add(setting);

        Dimension preferredSize = new Dimension(70, 40);//设置尺寸
        git.setPreferredSize(preferredSize);
        tools.setPreferredSize(preferredSize);
        setting.setPreferredSize(preferredSize);


        GitPanel gitPanel = new GitPanel();
        ToolsPanel toolsPanel = new ToolsPanel();
        SettingPanel settingPanel = new SettingPanel();


        gitPanel.setVisible(true);
        toolsPanel.setVisible(false);
        settingPanel.setVisible(false);

        git.setClick(e -> {
            this.getContentPane().add(gitPanel, BorderLayout.CENTER);
            gitPanel.setVisible(true);
            toolsPanel.setVisible(false);
            settingPanel.setVisible(false);
        });

        tools.setClick(e -> {
            this.getContentPane().add(toolsPanel, BorderLayout.CENTER);
            gitPanel.setVisible(false);
            toolsPanel.setVisible(true);
            settingPanel.setVisible(false);
        });

        setting.setClick(e -> {
            this.getContentPane().add(settingPanel, BorderLayout.CENTER);
            gitPanel.setVisible(false);
            toolsPanel.setVisible(false);
            settingPanel.setVisible(true);
        });

        this.getContentPane().add(btnBox, BorderLayout.PAGE_START);
        this.getContentPane().add(gitPanel, BorderLayout.CENTER);
    }

}


