/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;

/**
 *
 * @author Hoang Tran < hoang at 99.hoangtran@gmail.com >
 */
public class ClientManager {

    ArrayList<SocketHandlerServerSide> clients;

    public ClientManager() {
        clients = new ArrayList<>();
    }

    public boolean add(SocketHandlerServerSide c) {
        if (!clients.contains(c)) {
            clients.add(c);
            return true;
        }
        return true;
    }

    public boolean remove(SocketHandlerServerSide c) {
        if (clients.contains(c)) {
            clients.remove(c);
            return true;
        }
        return false;
    }

    public SocketHandlerServerSide find(String name) {
        for (SocketHandlerServerSide c : clients) {
            if (c.clientName.equals(name)) {
                return c;
            }
        }
        return null;
    }

    public void broadcast(String msg) {
        clients.forEach((c) -> {
            c.sendData(msg);
        });
    }

    public int getSize() {
        return clients.size();
    }
}
