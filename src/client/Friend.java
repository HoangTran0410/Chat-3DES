/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.ArrayList;
import shared.Helper;

/**
 *
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 */
public class Friend {

    String _name;
    String _lastChat;
    ArrayList<String> _chatHistory;

    public Friend(String name) {
        _name = name;
        _lastChat = "online";
        _chatHistory = new ArrayList<>();

        // thêm tên bạn bè vào đầu tin nhắn
        _chatHistory.add("------ " + name + "------ \n");
    }

    public void addChat(String sender, String chatContent) {
        String dateTime = Helper.getCurrentDateTime();
        _chatHistory.add(sender + " - " + dateTime + "\n> " + chatContent + "\n");

        int contentLen = chatContent.length();
        int maxLen = 20;

        _lastChat = chatContent.substring(0, Math.min(contentLen, maxLen));
        _lastChat += contentLen > maxLen ? "..." : "";
    }

    public String getChatHistory() {
        return String.join("\n", _chatHistory);
    }

    public String getLastChat() {
        return _lastChat;
    }

    public String getName() {
        return _name;
    }
}
