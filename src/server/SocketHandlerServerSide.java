/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import shared.Constants;
import shared.Helper;
import shared.SocketHandlerBase;
import shared.tripleDES.TripleDES;

/**
 *
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 */
public class SocketHandlerServerSide extends SocketHandlerBase implements Runnable {

    public String clientName;

    public SocketHandlerServerSide(Socket s) throws IOException {
        super(s);
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            try {
                // wait for receive data from client
                String received = dis.readUTF();

                if (clientName == null) {
                    onReceiveClientData(received);
                } else {
                    String decrypted = decryptReceivedData(received);
                    String type = getReceivedType(decrypted);

                    // check event
                    if (type.equals(Constants.CHAT_EVENT)) {
                        onReceiveChatData(decrypted);
                    }
                }
            } catch (Exception ex) {
                //Logger.getLogger(SocketHandlerServerSide.class.getName()).log(Level.SEVERE, null, ex);
                running = false;
            }
        }

        System.out.println("- DISCONNECTED " + clientName);
        RunServer.clientManager.remove(this);
        broadcastOnlineList();
        closeResources();
    }

    private static void broadcastOnlineList() {
        // broadcast online list back to all clients
        String onlineListData = Helper.pack(Constants.ONLINE_LIST_EVENT, RunServer.clientManager.getOnlineName());
        RunServer.clientManager.broadcast(onlineListData);
        System.out.println("SENT online list to ALL clients: " + onlineListData);
    }

    private void onReceiveClientData(String received) throws Exception {
        // cast to byte
        byte[] crypted = Helper.base64Decode(received);

        // decrypt received using rsa
        String decrypted = new String(RunServer.rsa.decrypt(crypted));

        // read client data
        System.out.println("RECEIVED Client Data: " + decrypted);
        ArrayList<String> clientData = Helper.unpack(decrypted);
        String name = clientData.get(1);
        String key1 = clientData.get(2);
        String key2 = clientData.get(3);
        String key3 = clientData.get(4);

        // save
        clientName = name;
        tripleDES = new TripleDES(key1, key2, key3);
        System.out.println("=> Saved client data.");

        broadcastOnlineList();
    }

    private void onReceiveChatData(String decrypted) {
        System.out.println("RECEIVED chat data: " + decrypted);

        // get receiver name
        ArrayList<String> chatData = Helper.unpack(decrypted);
        String senderName = chatData.get(1);
        String receiverName = chatData.get(2);
        String chatContent = chatData.get(3);

        // send chat to receiver and sender
        SocketHandlerServerSide receiver = RunServer.clientManager.find(receiverName);
        receiver.sendData(decrypted);
        System.out.println("SENT chat data to receiver: " + receiverName);

        SocketHandlerServerSide sender = RunServer.clientManager.find(senderName);
        sender.sendData(decrypted);
        System.out.println("SENT chat data to sender: " + senderName);
    }
}
