/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.Friend;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 * https://cachhoc.net/2014/04/25/java-swing-tuy-bien-jlist-jlist-custom-renderer/
 */
public class FriendRenderer extends JPanel implements ListCellRenderer<Friend> {

    private static final Color HIGHLIGHT_COLOR = new Color(184, 207, 229);
    private final static ImageIcon ava = new ImageIcon("src/client/gui/assets/icons8_male_user_32px.png");

    private final JLabel lbIcon = new JLabel();
    private final JLabel lbName = new JLabel();
    private final JLabel lbLastChat = new JLabel();

    public FriendRenderer() {
        setLayout(new BorderLayout(5, 5));

        JPanel panelText = new JPanel(new GridLayout(0, 1));
        panelText.add(lbName);
        panelText.add(lbLastChat);
        add(lbIcon, BorderLayout.WEST);
        add(panelText, BorderLayout.CENTER);
        
        lbLastChat.setForeground(Color.GRAY);

        setOpaque(true);
        lbIcon.setOpaque(true);
        lbLastChat.setOpaque(true);
        lbName.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Friend> list,
            Friend friend, int index, boolean isSelected, boolean cellHasFocus) {

        lbIcon.setIcon(ava);
        lbName.setText(friend.getName());
        lbLastChat.setText(friend.getLastChat());

        // when select item
        if (isSelected) {
            lbIcon.setBackground(HIGHLIGHT_COLOR);
            lbName.setBackground(HIGHLIGHT_COLOR);
            lbLastChat.setBackground(HIGHLIGHT_COLOR);
            setBackground(HIGHLIGHT_COLOR);
        } else {
            lbIcon.setBackground(Color.white);
            lbName.setBackground(Color.white);
            lbLastChat.setBackground(Color.white);
            setBackground(Color.white);
        }

        // seen
        if (friend.uneenCount != 0) {
            lbName.setForeground(Color.red);
            lbName.setText(friend.getName() + " (" + friend.uneenCount + ")");
        } else {
            lbName.setForeground(Color.BLACK);
        }

        return this;
    }
}
