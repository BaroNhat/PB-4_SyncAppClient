/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UI;


import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import syncClient.Client;

public class formClient extends JFrame {
    Client client;

    public String getFolderPath() {
        return selectedFolderPath;
    }

    private static formClient instance;

    public void setFormClient(formClient formClient) {
        instance = formClient;
    }

    public static formClient getInstance() {
        return instance;
    }

    private volatile static String selectedFolderPath = null;

    public formClient() {
        initComponents();
        setVisible(true);
    }

    public formClient(String message) {
        showMessage(message);
    }

    public void connectToServer(String folderPath, String IpAddress, int port) {
        client = new Client(IpAddress, port, folderPath);
        client.connect();
    }

    public void startListenServer() {
        client.startListener();
    }

    public void requestSync() {
        JOptionPane.showMessageDialog(this,

                "You must synchronize before working.",

                "Important",

                JOptionPane.WARNING_MESSAGE);
        client.requestSyncFromServer();

        setTableData(selectedFolderPath, "");
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this,

                message,

                "Notifications",

                JOptionPane.INFORMATION_MESSAGE);
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        txtPath = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnBrowse = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnShow = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnDELETE = new javax.swing.JButton();
        btnEditFolder = new javax.swing.JButton();
        Parent = new javax.swing.JPanel();
        firstPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        secondPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFilename = new javax.swing.JTextField();
        txtFolderPath = new javax.swing.JTextField();
        btnSelectFile = new javax.swing.JButton();
        btnAddFile = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override

            public void windowClosing(WindowEvent event) {
                if (selectedFolderPath == null) System.exit(0);
                // Thực hiện hành động bạn muốn trước khi ứng dụng tắt
                JOptionPane.showMessageDialog(Parent,

                        "You must synchronize before disconnect.",

                        "Important", // Tiêu đề của thông báo

                        JOptionPane.WARNING_MESSAGE);

                client.SyncToServer();
                setTableData(selectedFolderPath, "");
                client.messageClose();
                final JOptionPane optionPane = new JOptionPane("Application will shut down in 10 seconds...",

                        JOptionPane.WARNING_MESSAGE,

                        JOptionPane.DEFAULT_OPTION,

                        null,

                        new Object[]{}, // Tùy chọn bị loại bỏ

                        null);

                // Tạo một JDialog và hiển thị thông báo

                final JDialog dialog = new JDialog();

                dialog.setTitle("Closing Application");

                dialog.setModal(true);

                // Tạo một Timer để cập nhật thông báo đó

                Timer timer = new Timer(1000, new ActionListener() {

                    int timeLeft = 10;

                    @Override

                    public void actionPerformed(ActionEvent e) {

                        if (timeLeft > 0) {

                            optionPane.setMessage("Application will shut down in " + timeLeft + " seconds...");

                            timeLeft--;

                        } else {

                            ((Timer) e.getSource()).stop(); // Ngưng Timer

                            dialog.dispose(); // Đóng Dialog

                            System.exit(0);  // Tắt ứng dụng

                        }

                    }

                });

                timer.setInitialDelay(0);

                timer.start();

                dialog.setContentPane(optionPane);

                dialog.pack();

                dialog.setLocationRelativeTo(Parent);

                dialog.setVisible(true);

                // Đoạn mã này sẽ làm cho dialog biến mất sau khi Timer hoàn thành

                dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

            }

        });
        setTitle("CLIENT FORM");
        setBounds(new java.awt.Rectangle(300, 50, 0, 0));

        jPanel1.setBackground(new java.awt.Color(240, 248, 240));
        jPanel1.setForeground(new java.awt.Color(250, 248, 240));
        jPanel1.setName("header"); // NOI18N

        txtSearch.setFont(new java.awt.Font("", 0, 14)); // NOI18N
        txtSearch.setForeground(new java.awt.Color(153, 153, 153));
        txtSearch.setText("search file");
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSearchFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSearchFocusLost(evt);
            }
        });

        txtPath.setEditable(false);
        txtPath.setBackground(new java.awt.Color(255, 255, 255));
        txtPath.setFont(new java.awt.Font("", 0, 14)); // NOI18N
        txtPath.setForeground(new java.awt.Color(153, 153, 153));
        txtPath.setText("Select the path to sync folder");

        btnSearch.setBackground(new java.awt.Color(240, 248, 240));
        btnSearch.setForeground(new java.awt.Color(250, 248, 240));
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Search.png"))); // NOI18N
        btnSearch.setBorderPainted(false);
        btnSearch.setContentAreaFilled(false);
        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSearchMouseClicked(evt);
            }
        });

        btnBrowse.setBackground(new java.awt.Color(240, 248, 240));
        btnBrowse.setForeground(new java.awt.Color(250, 248, 240));
        btnBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Opened Folder.png"))); // NOI18N
        btnBrowse.setBorder(null);
        btnBrowse.setBorderPainted(false);
        btnBrowse.setContentAreaFilled(false);
        btnBrowse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBrowse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBrowseMouseClicked(evt);
            }
        });

        btnRefresh.setBackground(new java.awt.Color(240, 248, 240));
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Sync.png"))); // NOI18N
        btnRefresh.setBorder(null);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setContentAreaFilled(false);
        btnRefresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefresh.setDefaultCapable(false);
        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshMouseClicked(evt);
            }
        });

        btnShow.setBackground(new java.awt.Color(240, 248, 240));
        btnShow.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        btnShow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Home.png"))); // NOI18N
        btnShow.setBorderPainted(false);
        btnShow.setContentAreaFilled(false);
        btnShow.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnShow.setPreferredSize(new java.awt.Dimension(75, 25));
        btnShow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnShowMouseClicked(evt);
            }
        });

        btnAdd.setBackground(new java.awt.Color(240, 248, 240));
        btnAdd.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Add File.png"))); // NOI18N
        btnAdd.setBorderPainted(false);
        btnAdd.setContentAreaFilled(false);
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd.setPreferredSize(new java.awt.Dimension(75, 25));
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddMouseClicked(evt);
            }
        });

        btnDELETE.setBackground(new java.awt.Color(240, 248, 240));
        btnDELETE.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        btnDELETE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Delete File.png"))); // NOI18N
        btnDELETE.setBorderPainted(false);
        btnDELETE.setContentAreaFilled(false);
        btnDELETE.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDELETE.setPreferredSize(new java.awt.Dimension(75, 25));
        btnDELETE.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDELETEMouseClicked(evt);
            }
        });

        btnEditFolder.setBackground(new java.awt.Color(240, 248, 240));
        btnEditFolder.setForeground(new java.awt.Color(250, 248, 240));
        btnEditFolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Edit Folder.png"))); // NOI18N
        btnEditFolder.setBorder(null);
        btnEditFolder.setBorderPainted(false);
        btnEditFolder.setContentAreaFilled(false);
        btnEditFolder.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditFolder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditFolderMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnShow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txtPath, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnBrowse)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnEditFolder)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(1, 1, 1)
                                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(6, 6, 6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnRefresh)
                                        .addComponent(btnDELETE, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(17, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(2, 2, 2)
                                                                .addComponent(btnRefresh))
                                                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(txtPath, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnBrowse)))
                                        .addComponent(btnDELETE, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnAdd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnShow, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnEditFolder, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addContainerGap())
        );

        Parent.setLayout(new java.awt.CardLayout());

        firstPanel.setBackground(new java.awt.Color(240, 248, 240));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout firstPanelLayout = new javax.swing.GroupLayout(firstPanel);
        firstPanel.setLayout(firstPanelLayout);
        firstPanelLayout.setHorizontalGroup(
                firstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, firstPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
                                .addContainerGap())
        );
        firstPanelLayout.setVerticalGroup(
                firstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(firstPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(7, Short.MAX_VALUE))
        );

        Parent.add(firstPanel, "card2");

        secondPanel.setBackground(new java.awt.Color(240, 248, 240));
        secondPanel.setForeground(new java.awt.Color(250, 248, 240));
        secondPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel1.setBackground(new java.awt.Color(255, 255, 204));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Folder Path");

        jLabel2.setBackground(new java.awt.Color(255, 255, 204));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Filename");

        txtFilename.setEditable(false);
        txtFilename.setBackground(new java.awt.Color(255, 255, 255));
        txtFilename.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFilename.setForeground(new java.awt.Color(153, 153, 153));
        txtFilename.setText("Filename");

        txtFolderPath.setEditable(false);
        txtFolderPath.setBackground(new java.awt.Color(255, 255, 255));
        txtFolderPath.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFolderPath.setForeground(new java.awt.Color(153, 153, 153));
        txtFolderPath.setText("Folder Path");

        btnSelectFile.setBackground(new java.awt.Color(242, 242, 242));
        btnSelectFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Add File.png"))); // NOI18N
        btnSelectFile.setBorder(null);
        btnSelectFile.setBorderPainted(false);
        btnSelectFile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSelectFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSelectFileMouseClicked(evt);
            }
        });

        btnAddFile.setBackground(new java.awt.Color(242, 242, 242));
        btnAddFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Plus Math.png"))); // NOI18N
        btnAddFile.setBorder(null);
        btnAddFile.setBorderPainted(false);
        btnAddFile.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddFileMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout secondPanelLayout = new javax.swing.GroupLayout(secondPanel);
        secondPanel.setLayout(secondPanelLayout);
        secondPanelLayout.setHorizontalGroup(
                secondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(secondPanelLayout.createSequentialGroup()
                                .addGap(156, 156, 156)
                                .addGroup(secondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(secondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtFilename)
                                        .addComponent(txtFolderPath, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSelectFile, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(117, 117, 117))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, secondPanelLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAddFile, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(300, 300, 300))
        );
        secondPanelLayout.setVerticalGroup(
                secondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(secondPanelLayout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addGroup(secondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtFolderPath, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(secondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(secondPanelLayout.createSequentialGroup()
                                                .addGap(31, 31, 31)
                                                .addGroup(secondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtFilename, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                        .addGroup(secondPanelLayout.createSequentialGroup()
                                                .addGap(17, 17, 17)
                                                .addComponent(btnSelectFile, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addComponent(btnAddFile, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(179, 179, 179))
        );

        Parent.add(secondPanel, "card3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(Parent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(Parent, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.getAccessibleContext().setAccessibleName("");
        jPanel1.getAccessibleContext().setAccessibleDescription("header");

        pack();
    }


    private static void showFileChooser(JFrame frame, JTextField txtPath) {
        JFileChooser fileChooser = new JFileChooser();
        while (true) {

            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                break;
            } else if (!txtPath.getText().equals("Select the path to sync folder")) {
                return;
            } else {
                JOptionPane.showMessageDialog(frame, "Error: Please choose Folder to Synchronize", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        String selectedPath = fileChooser.getSelectedFile().getAbsolutePath();

        if (selectedPath.equals(selectedFolderPath)) {
            return;
        }

        txtPath.setText(selectedPath);
        txtPath.setForeground(Color.black);
        selectedFolderPath = selectedPath;

        System.out.println("selectedPath: " + selectedFolderPath);

    }

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseClicked
        // TODO add your handling code here:

        if (txtPath.getText().equals("Select the path to sync folder")) {
            JOptionPane.showMessageDialog(this,
                    "Error: Please choose a folder to add",
                    "Error", JOptionPane.ERROR_MESSAGE);
            showFileChooser(this, txtPath);
        } else {
            txtFolderPath.setText(txtPath.getText());
            Parent.removeAll();
            Parent.add(secondPanel);
            Parent.repaint();
            Parent.revalidate();
        }
    }//GEN-LAST:event_btnAddMouseClicked

    private void btnShowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnShowMouseClicked
        // TODO add your handling code here:
        Parent.removeAll();
        Parent.add(firstPanel);
        Parent.repaint();
        Parent.revalidate();
        setTableData(selectedFolderPath, "");
    }//GEN-LAST:event_btnShowMouseClicked

    private void txtSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSearchFocusGained
        // TODO add your handling code here:
        if (txtSearch.getText().equals("search file")) {
            txtSearch.setText("");
            txtSearch.setForeground(Color.black);
        }
    }//GEN-LAST:event_txtSearchFocusGained

    private void txtSearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSearchFocusLost
        // TODO add your handling code here:
        if (txtSearch.getText().equals("")) {
            txtSearch.setText("search file");
            txtSearch.setForeground(new Color(153, 153, 153));
        }
    }//GEN-LAST:event_txtSearchFocusLost

    private void btnSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseClicked
        // TODO add your handling code here:
        if (txtSearch.getText().equals("search file")) {
            setTableData(selectedFolderPath, "");
            txtSearch.requestFocus();
        } else {
            System.out.println(txtSearch.getText());
            setTableData(selectedFolderPath, txtSearch.getText());
        }
    }//GEN-LAST:event_btnSearchMouseClicked

    private void btnBrowseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBrowseMouseClicked
        // TODO add your handling code here:
        showFileChooser(this, txtPath);
        setTableData(selectedFolderPath, "");
    }//GEN-LAST:event_btnBrowseMouseClicked

    private void btnEditFolderMouseClicked(MouseEvent evt) {
        if (selectedFolderPath == null) {
            int option = JOptionPane.showOptionDialog(this,
                    "Folder was not selected.\nPlease select Folder to Synchronize",
                    "Error", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);

            if (option == JOptionPane.OK_OPTION)
                showFileChooser(this, txtPath);
        } else {
            try {
                File folder = new File(selectedFolderPath);

                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.OPEN)) {
                        desktop.open(folder);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void btnRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshMouseClicked
        // TODO add your handling code here:
        if ("Select the path to sync folder".equals(txtPath.getText())) {
            int option = JOptionPane.showOptionDialog(this,
                    "Error: Please select the path to sync folder",
                    "Error", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);

            // Kiểm tra nếu người dùng nhấp vào OK
            if (option == JOptionPane.OK_OPTION) {
                showFileChooser(this, txtPath);
            }
        } else {
            int option = JOptionPane.showOptionDialog(this,
                    "SUCCESS: Sync Files To Server",
                    "INFORMATION", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
            if (option == JOptionPane.OK_OPTION) {
//                Thread syncThread = new Thread(() -> {
//                        client.SyncToServer();
//                });
//                syncThread.start();
                client.SyncToServer();
            }
        }
    }//GEN-LAST:event_btnRefreshMouseClicked

    private void btnSelectFileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSelectFileMouseClicked
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        // Nếu người dùng chọn file, lưu đường dẫn vào txtFilename
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtFilename.setText(selectedFile.getAbsolutePath());
        }
    }//GEN-LAST:event_btnSelectFileMouseClicked


    private void btnAddFileMouseClicked(java.awt.event.MouseEvent evt) {
// need fix duplicate case
        String sourceFilePath = txtFilename.getText(); // Đường dẫn tới tệp muốn thêm
        if (sourceFilePath.equals("Filename")) {
            JOptionPane.showOptionDialog(this,
                    "FILE PATH EMPTY!!!",
                    "Error", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
            return;
        }
        try {
            Path sourcePath = Paths.get(sourceFilePath);
            Path targetPath = Paths.get(selectedFolderPath, sourcePath.getFileName().toString());

            String sourceFileName = sourcePath.getFileName().toString();

            ArrayList<File> allFiles = new ArrayList<>();

            File folder = new File(selectedFolderPath);
            File[] listOfFiles = folder.listFiles();

            if (listOfFiles != null) {

                for (File file : listOfFiles) {

                    if (file.isFile()) {

                        allFiles.add(file);

                    }

                }

            }

            boolean isFileNameExist = false;

            for (File file : allFiles) {

                if (file.getName().equals(sourceFileName)) {

                    isFileNameExist = true;

                    break;  // Nếu tìm thấy tên tệp, thoát khỏi vòng lặp

                }

            }

            if (isFileNameExist) {
                JOptionPane.showOptionDialog(this,
                        "FILE EXIST!!!",
                        "Error", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
                return;
            }

            int option = JOptionPane.showConfirmDialog(this, "Confirm Add File.!", "INFORM", JOptionPane.ERROR_MESSAGE);

            // Kiểm tra nếu người dùng nhấp vào OK
            if (option == JOptionPane.OK_OPTION) {


                Files.copy(sourcePath, targetPath);
                System.out.println("File was added in to folder: " + selectedFolderPath);

                Parent.removeAll();
                Parent.add(firstPanel);
                Parent.repaint();
                Parent.revalidate();
                setTableData(selectedFolderPath, "");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error adding file.");
        }

    }

    private void btnDELETEMouseClicked(java.awt.event.MouseEvent evt) {

        int[] selectedRows = jTable1.getSelectedRows(); // Lấy chỉ mục của các hàng đã chọn
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(firstPanel, "SELECT FILES TO DELETE", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showOptionDialog(this,
                "Warning: CONFIRM DELETE FILE!",
                "Warning", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);

        if (option == JOptionPane.OK_OPTION) {
            for (int row : selectedRows) {
                String fileName = jTable1.getValueAt(row, 0).toString(); // Lấy tên tệp từ cột đầu tiên (cột "File name")
                String filePath = selectedFolderPath + File.separator + fileName; // Đường dẫn tới tệp cần xóa

                File fileToDelete = new File(filePath);

                if (fileToDelete.delete()) {
                    System.out.println("Đã xóa tệp: " + fileName);
                } else {
                    System.out.println("Không thể xóa tệp: " + fileName);
                }
            }
        }


        setTableData(selectedFolderPath, "");
    }//GEN-LAST:event_btnDELETEMouseClicked

    private void txtDeleteFileFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDeleteFileFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDeleteFileFocusGained

    private void txtDeleteFileFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDeleteFileFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDeleteFileFocusLost

    private void btnSelectFile1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSelectFile1MouseClicked
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File(txtPath.getText()));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(this);

        // Nếu người dùng chọn file, lưu đường dẫn vào txtFilename
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtDeleteFile.setText(selectedFile.getAbsolutePath());
        }
    }//GEN-LAST:event_btnSelectFile1MouseClicked


    public void setTableData(String folderPath, String filename) {
        File folder = new File(folderPath);

        if (!folder.isDirectory()) {
            System.out.println("Không phải thư mục hợp lệ.");
            return;
        }

        File[] files = folder.listFiles();

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("File name");
        model.addColumn("Size(KB)");
        model.addColumn("Date Modified");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.contains(filename)) { // Kiểm tra xem tên tệp có chứa filename hay không
                double fileSize = (double) file.length() / 1024;
                String modifiedDate = dateFormat.format(file.lastModified());

                model.addRow(new Object[]{fileName, fileSize, modifiedDate});
            }
        }

        jTable1.setModel(model);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel Parent;
    private JButton btnAdd;
    private JButton btnAddFile;
    private JButton btnBrowse;
    private JButton btnDELETE;
    private JButton btnEditFolder;
    private JButton btnRefresh;
    private JButton btnSearch;
    private JButton btnSelectFile;
    private JButton btnSelectFile1;
    private JButton btnShow;
    private JPanel firstPanel;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JScrollPane jScrollPane1;
    private static JTable jTable1;
    private JPanel secondPanel;
    private JPanel thirdPanel;
    private JTextField txtDeleteFile;
    private JTextField txtFilename;
    private JTextField txtFolderPath;
    private JTextField txtPath;
    private JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}