/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import shared.tripleDES.TripleDES;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
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

    Thread listener = null;
    String name;

    public void connect(String addr, int port, String name) throws Exception {
        this.name = name;

        // getting ip 
        InetAddress ip = InetAddress.getByName(addr);

        // establish the connection with server port 
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

        // create new key
        String key1 = Helper.randomString(5, 10);
        String key2 = Helper.randomString(5, 10);
        String key3 = Helper.randomString(5, 10);

        // init 3Des instance
        tripleDES = new TripleDES(key1, key2, key3);

        // prepare client data with format "event_key;name;key1;key2;key3"
        String clientData = Helper.createClientData(name, key1, key2, key3);

        // encrypt 3des-keys using rsa with server public-key 
        RSA clientSideRSA = new RSA().preparePublicKey(Constants.RSA_PUBLICKEY_PATH);
        byte[] clientDataEncrypted = clientSideRSA.encrypt(clientData.getBytes());

        // send to server with base64 format
        String clientDataEncryptedStr = Helper.base64Encode(clientDataEncrypted);
        sendPureData(clientDataEncryptedStr);

        System.out.println("SENT CLIENT DATA: " + clientData);
    }

    public void sendChat(String receiver, String content) {
        String chatData = Helper.createChatData(name, receiver, content);
        sendData(chatData);
        System.out.println("SENT CHAT DATA: " + chatData);
    }

    private void onReceiveChatData(String decrypted) {
        System.out.println("RECEIVE CHAT DATA: " + decrypted);
    }

    public void getAllOtherClientNames() {

    }

    public String getName() {
        return name;
    }
}
