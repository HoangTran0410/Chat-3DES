/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client.gui.ClientChatForm;

/**
 *
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 */
public class RunClient {    
    public static void main(String[] args) throws Exception {
        //connectWithoutGUI("Hoang");

        new ClientChatForm();
    }
}
