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
import javax.swing.JOptionPane;
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

    public void connect(String addr, int port) throws Exception {
        // getting ip 
        InetAddress ip = InetAddress.getByName(addr);

        // establish the connection with server port 
        socket = new Socket();
        socket.connect(new InetSocketAddress(ip, port), 4000);
        System.out.println("Connected to " + ip + ":" + port + ", localport:" + socket.getLocalPort());

        // obtaining input and output streams
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());

        // close old listener
        if (listener != null && listener.isAlive()) {
            listener.interrupt();
        }

        // listen to server
        listener = new Thread(this::listen);
        listener.start();

        // security
        init3DES();
    }

    private void listen() {
        boolean running = true;

        while (running) {
            try {
                String[] received = getReceivedData();
                String type = received[0];
                String data = received[1];

                // check event
                if (type.equals(Constants.CHAT_EVENT)) {

                }

            } catch (IOException ex) {
                Logger.getLogger(SocketHandlerClientSide.class.getName()).log(Level.SEVERE, null, ex);
                running = false;
            }
        }

        closeResources();

        // alert if connect interup
        JOptionPane.showMessageDialog(null, "Mất kết nối tới server", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void init3DES() throws Exception {
        // create new key
        String key1 = Helper.randomString(5, 10);
        String key2 = Helper.randomString(5, 10);
        String key3 = Helper.randomString(5, 10);

        // init 3Des instance
        tripleDES = new TripleDES(key1, key2, key3);

        // encrypt 3des-keys using rsa with server'socket public-key 
        RSA clientSideRSA = new RSA().preparePublicKey("/src/server/rsa-key/publicKey");

        String tDesKey = String.join(Constants.SEPARATE_MARKER, key1, key2, key3);
        byte[] tDesKeyEncrypted = clientSideRSA.encrypt(tDesKey.getBytes());
        String tDesKeyEncryptedStr = Helper.base64Encode(tDesKeyEncrypted);

        // send to server
        sendPureData(Constants.DES_KEY_EVENT + Constants.SEPARATE_MARKER + tDesKeyEncryptedStr);
    }

}
