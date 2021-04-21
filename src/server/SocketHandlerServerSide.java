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
import javax.swing.JOptionPane;
import shared.Constants;
import shared.SocketHandlerBase;

/**
 *
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 */
public class SocketHandlerServerSide extends SocketHandlerBase implements Runnable {

    public SocketHandlerServerSide(Socket s) throws IOException {
        super(s);
    }

    @Override
    public void run() {

        boolean running = true;

        while (running) {
            try {
                String[] received = getReceivedData();
                String type = received[0];
                String data = received[1];

                // check event
                if (type.equals(Constants.DES_KEY_EVENT)) {
                    onReceiveDESKey(data);
                } else if (type.equals(Constants.CHAT_EVENT)) {

                }

            } catch (IOException ex) {
                Logger.getLogger(SocketHandlerServerSide.class.getName()).log(Level.SEVERE, null, ex);
                running = false;
            }
        }

        closeResources();

        // alert if connect interup
        JOptionPane.showMessageDialog(null, "Mất kết nối tới client", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void onReceiveDESKey(String data) {

    }
}
