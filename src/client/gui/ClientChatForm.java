/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.Friend;
import client.SocketHandlerClientSide;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.DefaultCaret;
import shared.Constants;

/**
 *
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 */
public class ClientChatForm extends javax.swing.JFrame {

    SocketHandlerClientSide socketHandler;

    DefaultListModel<Friend> listFriendsModel;
    Friend currentFriend;

    /**
     * Creates new form ClientChatForm
     */
    public ClientChatForm() {
        try {
            initComponents();
            setTitle("Chat-3DES");
            setVisible(true);
            setLocationRelativeTo(null);

            initJListFriends();
            initChatArea();

            initSocket();
        } catch (Exception ex) {
            Logger.getLogger(ClientChatForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi " + ex.getMessage());
        }
    }

    // -------------------------- Initialize --------------------------
    private void initSocket() throws Exception {
        String name = JOptionPane.showInputDialog("Tên của bạn?", "Hoang");

        lbUserName.setText(name);

        socketHandler = new SocketHandlerClientSide(this);
        socketHandler.connect("localhost", Constants.SERVER_PORT, name);
    }

    private void initJListFriends() {
        listFriendsModel = new DefaultListModel<>();
        lFriends.setModel(listFriendsModel);
        lFriends.setCellRenderer(new FriendRenderer());

        // https://stackoverflow.com/a/13800933/11898496
        lFriends.addListSelectionListener((ListSelectionEvent arg0) -> {
            if (!arg0.getValueIsAdjusting()) {
                Friend f = lFriends.getSelectedValue();
                if (f != null) {
                    txChatHistory.setText(f.getChatHistory());
                    currentFriend = f;
                    currentFriend.uneenCount = 0;
                }
            }
        });
    }

    private void initChatArea() {
        // on press Enter
        txChatInput.addActionListener((ae) -> {
            sendChat();
        });

        // https://stackoverflow.com/a/9000922/11898496
        DefaultCaret caret = (DefaultCaret) txChatHistory.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    // -------------------------- Chat area --------------------------
    private void sendChat() {
        if (currentFriend == null) {
            JOptionPane.showMessageDialog(this, "Chưa chọn bạn bè nào");
            return;
        }

        String sender = socketHandler.getName();
        String content = txChatInput.getText();

        socketHandler.sendChat(currentFriend.getName(), content);

        txChatInput.setText("");
//        currentFriend.addChat(sender, content);
    }

    // -------------------------- Socket events --------------------------
    public void updateOnlineList(ArrayList<String> onlineNames) {
        // remove my name
        onlineNames.removeIf(name -> name.equals(socketHandler.getName()));

        // remove if not found in onlineNames
        for (int i = listFriendsModel.getSize() - 1; i >= 0; i--) {
            boolean found = false;
            String fname = listFriendsModel.get(i).getName();
            for (String oname : onlineNames) {
                if (fname.equals(oname)) {
                    found = true;
                }
            }

            if (!found) {
                // current friend offline
                if (fname.equals(currentFriend.getName())) {
                    txChatHistory.setText(txChatHistory.getText() + "\n- " + fname + " ĐÃ THOÁT");
                    currentFriend = null;
                }
                listFriendsModel.remove(i);
            }
        }

        // add name if not found in listFriendsModel
        onlineNames.forEach((name) -> {
            boolean found = false;
            for (int i = 0; i < listFriendsModel.getSize(); i++) {
                if (listFriendsModel.get(i).getName().equals(name)) {
                    found = true;
                }
            }
            if (!found) {
                listFriendsModel.addElement(new Friend(name));
            }
        });
    }

    public void addChat(String friendName, String senderName, String content) {
        Friend f = null;
        for (int i = 0; i < listFriendsModel.getSize(); i++) {
            Friend fi = listFriendsModel.elementAt(i);
            if (fi.getName().equals(friendName)) {
                fi.addChat(senderName, content);
                f = fi;
                break;
            }
        }

        boolean isCurrentFriend = currentFriend != null && currentFriend == f;

        if (isCurrentFriend) {
            txChatHistory.setText(currentFriend.getChatHistory());
        } else {
            f.uneenCount++;

            // https://stackoverflow.com/a/4921271/11898496
            SwingUtilities.invokeLater(lFriends::updateUI);
        }
    }

    public void setLoadingState(boolean visible, String text) {
        lbLoading.setText(text);
        pbLoading.setVisible(visible);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txChatHistory = new javax.swing.JTextArea();
        txChatInput = new javax.swing.JTextField();
        btnSendChat = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lbUserName = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lbLoading = new javax.swing.JLabel();
        pbLoading = new javax.swing.JProgressBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        lFriends = new javax.swing.JList<>();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Tin nhắn"));

        txChatHistory.setEditable(false);
        txChatHistory.setColumns(20);
        txChatHistory.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txChatHistory.setRows(5);
        jScrollPane2.setViewportView(txChatHistory);

        txChatInput.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        btnSendChat.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnSendChat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/gui/assets/icons8_paper_plane_32px.png"))); // NOI18N
        btnSendChat.setText("Gửi");
        btnSendChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendChatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txChatInput)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSendChat)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSendChat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txChatInput))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbUserName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/gui/assets/icons8_male_user_32px.png"))); // NOI18N
        lbUserName.setText("User Name");

        lbLoading.setText("Đang tải");
        lbLoading.setName(""); // NOI18N

        pbLoading.setValue(100);
        pbLoading.setIndeterminate(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pbLoading, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbLoading, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(lbLoading)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pbLoading, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbUserName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Bạn bè online:"));

        lFriends.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lFriends.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPane1.setViewportView(lFriends);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendChatActionPerformed
        sendChat();
    }//GEN-LAST:event_btnSendChatActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientChatForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientChatForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientChatForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientChatForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientChatForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSendChat;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<Friend> lFriends;
    private javax.swing.JLabel lbLoading;
    private javax.swing.JLabel lbUserName;
    private javax.swing.JProgressBar pbLoading;
    private javax.swing.JTextArea txChatHistory;
    private javax.swing.JTextField txChatInput;
    // End of variables declaration//GEN-END:variables
}
