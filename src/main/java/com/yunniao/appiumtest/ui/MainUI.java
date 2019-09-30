package com.yunniao.appiumtest.ui;

import com.yunniao.appiumtest.ClientStarter;
import com.yunniao.appiumtest.utils.LogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by MrBu on 2016/4/6.
 */
public class MainUI extends JFrame {
    private static final String TEXT_GEN_CODE = "此处为log输出区域";
    private static final String IP_REMINDER = ClientStarter.url;
    private static final String UDID_REMINDER = "此处设备名,不填写为第一台连接设备";
    static TextArea taLog;
    JPanel panel1ForCode;
    JPanel panelBottomText;
    JPanel panelBottomBtn;
    Button btnStart;
    JTabbedPane tab;
    TextField tfUrl;
    private Font font;
    private Button btnRefresh;
    private Button btnStop;
    private FileTree fileTree;
    private TextField tfUdid;

    public MainUI() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");// 设置皮肤
        } catch (Exception e) {
            e.printStackTrace();
        }
        font = new Font(null, Font.BOLD | Font.ITALIC, 15);
        tab = new JTabbedPane(JTabbedPane.TOP);
        initUIForAppium();
        initTabs();
        initThis();
    }

    private void initThis() {
        setTitle("appium_client");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(350, 120);
        setAlwaysOnTop(false);
    }

    private void initTabs() {
        tab.add("appium_client", panel1ForCode);
        getContentPane().add(tab, BorderLayout.CENTER);
    }

    private void initUIForAppium() {
        DefaultListModel<String> defaultListModel = getListModel();
        final JList<String> list = new JList<>(defaultListModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileTree = new FileTree(true);
        fileTree.setShowHiddenFiles(false);
        fileTree.setDeleteEnabled(true);
        JScrollPane scrollPane = new JScrollPane(fileTree);
        panel1ForCode = new JPanel();
        panelBottomText = new JPanel();
        panelBottomBtn = new JPanel();
        btnStart = new Button("Start Test Work");
        btnStop = new Button("Stop Test Work");
        btnRefresh = new Button("refresh json file list");
        btnStop.setEnabled(false);
        tfUrl = new TextField(IP_REMINDER, 48);
        tfUdid = new TextField(UDID_REMINDER, 48);
        taLog = new TextArea(TEXT_GEN_CODE, 21, 70);
        panel1ForCode.setLayout(new BorderLayout());
        panelBottomText.setLayout(new BorderLayout());
        panel1ForCode.setBackground(Color.YELLOW);
        panel1ForCode.add(scrollPane, BorderLayout.WEST);
        taLog.setEditable(false);
        taLog.setFont(font);
        panel1ForCode.add(taLog, BorderLayout.CENTER);
        panelBottomText.add(tfUrl, BorderLayout.NORTH);
        panelBottomText.add(tfUdid, BorderLayout.CENTER);
        panelBottomBtn.add(btnRefresh);
        panelBottomBtn.add(btnStart);
        panelBottomBtn.add(btnStop);
        panelBottomText.add(panelBottomBtn, BorderLayout.SOUTH);
        panel1ForCode.add(panelBottomText, BorderLayout.SOUTH);
        initButtonEvent(list);
    }

    private DefaultListModel<String> getListModel() {
        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
        File[] fList = ClientStarter.instance.getFiles();
        if (fList != null) {
            for (int i = 0, len = fList.length; i < len; i++) {
                defaultListModel.addElement(fList[i].getName());
            }
        }
        return defaultListModel;
    }

    private void initButtonEvent(final JList<String> list) {
        btnStop.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ClientStarter.currentTest.stopDriver(true);
                } catch (IOException e1) {
                    LogUtil.e(e1);
                }
            }
        });
        btnRefresh.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileTree.updateRoot();
            }
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    File f = fileTree.getSelectedFile();
                    String udid = null;
                    if (f == null) {
                        tip("请至少选择一个json文件");
                        return;
                    }
                    String url = IP_REMINDER;
                    if (!tfUrl.getText().equals(IP_REMINDER)) {
                        url = tfUrl.getText();
                    }
                    String trim = tfUdid.getText().replace(UDID_REMINDER, "").trim();
                    if (trim.length() > 0) {
                        udid = trim;
                    }
                    taLog.setText("");
                    ClientStarter.instance.startTestFromUI(f, url, udid);
                } catch (IOException e) {
                    LogUtil.e(e);
                }
            }
        });
    }

    public void onTestStop(boolean stopAll) {
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
    }

    public void onTestStart() {
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
    }

    public void tip(String log) {
        taLog.setText(log);
    }

    public void log(String log) {
        taLog.append("\n" + log);
    }
}
