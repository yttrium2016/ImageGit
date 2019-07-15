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
class ToolsPanel extends JPanel {

    private InputPanel fileName = null;
    private InputPanel outPut = null;
    private String[] split = null;

    ToolsPanel() {
        initView();
        initData();
    }

    private void initData() {
        try {
            split = Config.getString("git.url", "").split("/");
            split[split.length - 1] = split[split.length - 1].contains(".") ? split[split.length - 1].substring(0, split[split.length - 1].lastIndexOf(".")) : split[split.length - 1];
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Git地址不正确 请重新填写", "", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void initView() {
        this.setBackground(Color.white);
        this.setLayout(new GridLayout(5, 1));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        fileName = new InputPanel("输入文件名");
        outPut = new InputPanel("文件地址");
        fileName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        outPut.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JPanel btnBox = new JPanel();
        btnBox.setBackground(Color.white);
        btnBox.setLayout(new FlowLayout(FlowLayout.CENTER));

        btnBox.add(new YButton("转化为GitHub地址", e -> {
            //https://github.com/yttrium2016/Blog.git
            //https://raw.githubusercontent.com/yttrium2016/Blog/master/xx.md
            if (split != null && split.length > 2) {
                String str = "https://raw.githubusercontent.com/" + split[split.length - 2] + "/" + split[split.length - 1] + "/master/" + fileName.getValue();
                outPut.setText(str);
            } else {
                outPut.setText("");
            }
        }));
        btnBox.add(new YButton("转化为Gitee地址", e -> {
            //https://gitee.com/iyzy/Blog.git
            //https://gitee.com/iyzy/Blog/raw/master/xx.md
            if (split != null && split.length > 2) {
                String str = "https://gitee.com/" + split[split.length - 2] + "/" + split[split.length - 1] + "/raw/master/" + fileName.getValue();
                outPut.setText(str);
            } else {
                outPut.setText("");
            }
        }));

        btnBox.add(new YButton("清除", e -> {
            outPut.setText("");
        }));

        this.add(fileName);
        this.add(btnBox);
        this.add(outPut);

    }

}
