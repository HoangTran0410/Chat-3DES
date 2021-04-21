/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import shared.Constants;

/**
 *
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 */
public class RunClient {

    public RunClient(String name) throws Exception {
        SocketHandlerClientSide client = new SocketHandlerClientSide();
        client.connect("localhost", Constants.SERVER_PORT, name);
        client.sendChat("Hien", "Hello cục cưng");
    }

    public static void main(String[] args) throws Exception {
        new RunClient("Hoang");
    }
}
