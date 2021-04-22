/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client.gui.ClientChatForm;
import shared.tripleDES.TripleDES;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.Constants;
import shared.Helper;
import shared.SocketHandlerBase;
import shared.security.RSA;

/**
 *
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 */
public class SocketHandlerClientSide extends SocketHandlerBase {

    ClientChatForm guiContainer;
    Thread listener = null;
    String myName;

    public SocketHandlerClientSide(ClientChatForm container) {
        guiContainer = container;
    }

    public void connect(String addr, int port, String name) throws Exception {
        this.myName = name;

        // getting ip 
        InetAddress ip = InetAddress.getByName(addr);

        // establish the connection with server port 
        System.out.println("Connecting to " + ip + ":" + port + "...");
        guiContainer.setLoadingState(true, "Đang kết nối...");
        socket = new Socket();
        socket.connect(new InetSocketAddress(ip, port), 4000);
        System.out.println("Connected to " + ip + ":" + port + ", localport:" + socket.getLocalPort());

        // obtaining input and output streams
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());

        // listen to server
        listener = new Thread(this::listen);
        listener.start();

        // security
        sendClientData(name);
    }

    private void listen() {
        boolean running = true;

        while (running) {
            try {
                // wait for receive data from server
                String received = dis.readUTF();
                String decrypted = decryptReceivedData(received);
                String type = getReceivedType(decrypted);

                // check event
                if (type.equals(Constants.CHAT_EVENT)) {
                    onReceiveChatData(decrypted);
                } else if (type.equals(Constants.ONLINE_LIST_EVENT)) {
                    onReceiveOnlineList(decrypted);
                }

            } catch (IOException ex) {
                Logger.getLogger(SocketHandlerClientSide.class.getName()).log(Level.SEVERE, null, ex);
                running = false;
            }
        }

        closeResources();
    }

    private void sendClientData(String name) throws Exception {
        System.out.println("Preparing client data...");
        guiContainer.setLoadingState(true, "Đang chuẩn bị dữ liệu...");

        // create new key
        String key1 = Helper.randomString(5, 10);
        String key2 = Helper.randomString(5, 10);
        String key3 = Helper.randomString(5, 10);

        // init 3Des instance
        tripleDES = new TripleDES(key1, key2, key3);

        // prepare client data with format "event_key;myName;key1;key2;key3"
        String clientData = Helper.pack(Constants.CLIENT_DATA_EVENT, name, key1, key2, key3);

        // encrypt 3des-keys using rsa with server public-key 
        RSA clientSideRSA = new RSA().preparePublicKey(Constants.RSA_PUBLICKEY_PATH);
        byte[] clientDataEncrypted = clientSideRSA.encrypt(clientData.getBytes());

        // send to server with base64 format
        String clientDataEncryptedStr = Helper.base64Encode(clientDataEncrypted);
        sendPureData(clientDataEncryptedStr);

        System.out.println("SENT CLIENT DATA: " + clientData);
        guiContainer.setLoadingState(true, "Đang tải danh sách bạn bè...");
    }

    public void sendChat(String receiver, String content) {
        String chatData = Helper.pack(Constants.CHAT_EVENT, myName, receiver, content);
        sendData(chatData);
        System.out.println("SENT CHAT DATA: " + chatData);
    }

    private void onReceiveChatData(String decrypted) {
        System.out.println("RECEIVED chat data: " + decrypted);

        ArrayList<String> chatData = Helper.unpack(decrypted);
        String senderName = chatData.get(1);
        String receiverName = chatData.get(2);
        String chatContent = chatData.get(3);

        String friendName = null;

        if (senderName.equals(myName)) {
            friendName = receiverName; // mình là người gửi
        } else if (receiverName.equals(myName)) {
            friendName = senderName; // mình là người nhận
        }

        if (friendName != null) {
            guiContainer.addChat(friendName, senderName, chatContent);
        } else {
            System.err.println("-ERROR: sender/receiver not valid. " + senderName + "/" + receiverName);
        }
    }

    private void onReceiveOnlineList(String decrypted) {
        System.out.println("RECEIVED online list: " + decrypted);

        ArrayList<String> onlineData = Helper.unpack(decrypted);
        onlineData.remove(0); // remove event name
        guiContainer.updateOnlineList(onlineData);
        guiContainer.setLoadingState(false, "");
    }

    public String getName() {
        return myName;
    }
}
