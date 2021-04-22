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
    public int uneenCount;

    public Friend(String name) {
        uneenCount = 0;
        _name = name;
        _lastChat = "online";
        _chatHistory = new ArrayList<>();

        // thêm tên bạn bè vào đầu tin nhắn
        _chatHistory.add("------ " + name + "------ \n");
    }

    public void addChat(String sender, String chatContent) {
        String dateTime = Helper.getCurrentDateTime();
        _chatHistory.add(
                dateTime + " - "
                + sender + "\n> "
                + Helper.wrapString(chatContent, 55) + "\n"
        );

        _lastChat = Helper.limitString(chatContent, 20);
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
