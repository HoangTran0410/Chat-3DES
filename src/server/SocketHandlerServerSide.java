/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
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
                Logger.getLogger(SocketHandlerServerSide.class.getName()).log(Level.SEVERE, null, ex);
                running = false;
            }
        }

        closeResources();
    }

    private void onReceiveClientData(String received) throws Exception {
        // cast to byte
        byte[] crypted = Helper.base64Decode(received);

        // decrypt received using rsa
        String decrypted = new String(RunServer.rsa.decrypt(crypted));

        // read client data
        System.out.println("RECEIVED Client Data: " + decrypted);
        String[] clientData = Helper.readClientData(decrypted);

        // save
        clientName = clientData[0];
        tripleDES = new TripleDES(clientData[1], clientData[2], clientData[3]);

        System.out.println("=> Saved client data.");
    }

    private void onReceiveChatData(String decrypted) {
        System.out.println("Receive chat data: " + decrypted);

        // TODO sent to target client
    }
}
