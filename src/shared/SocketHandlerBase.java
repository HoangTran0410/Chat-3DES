/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import client.SocketHandlerClientSide;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.tripleDES.TripleDES;

/**
 *
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 */
public class SocketHandlerBase {

    protected Socket socket;
    protected DataInputStream dis;
    protected DataOutputStream dos;

    protected TripleDES tripleDES = null;

    public SocketHandlerBase() {
    }

    public SocketHandlerBase(Socket s) throws IOException {
        this.socket = s;

        // obtaining input and output streams 
        this.dis = new DataInputStream(s.getInputStream());
        this.dos = new DataOutputStream(s.getOutputStream());
    }

    protected String[] getReceivedData() throws IOException {
        // receive the request from client
        String received = dis.readUTF();

        // decrypt data if needed
        if (tripleDES != null) {
            byte[] crypted = Helper.base64Decode(received);
            received = new String(tripleDES.decrypt(crypted));
        }

        System.out.println("RECEIVED: " + received);

        // process received data
        String[] splitted = received.split(Constants.SEPARATE_MARKER, 2);

        return splitted;
    }

    protected void closeResources() {
        try {
            // closing resources
            socket.close();
            dis.close();
            dos.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketHandlerClientSide.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // send data
    public void sendPureData(String data) {
        try {
            dos.writeUTF(data);

        } catch (IOException ex) {
            Logger.getLogger(SocketHandlerClientSide.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendData(String data) {
        try {
            byte[] encrypted = tripleDES.encrypt(data.getBytes());
            String strEncrypted = Helper.base64Encode(encrypted);
            dos.writeUTF(strEncrypted);

        } catch (IOException ex) {
            Logger.getLogger(SocketHandlerClientSide.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
