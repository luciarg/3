/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientemensajeria;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andres
 */
public class OutServer extends Thread {

    private ClientProject cp;
    private int port;
    private String ip;
    private boolean state;

    public OutServer(ClientProject cp, int port, String ip) {
        this.cp = cp;
        this.port = port;
        this.ip = ip;
        this.state = true;
    }

    //Intenta establecer una conexion si no tenemos un server y añadirlo.
    @Override
    public void run() {
        while (this.state) {
            try {
                Thread.sleep(1000);
                if (!cp.onServer()&&this.state) {
                    try {

                        Socket sock = new Socket(ip, port);
                        PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
                        out.println("$CLIENT$"+cp.getName()+"$");
                        cp.addServer(sock);

                    } catch (IOException ex) {
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(OutServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setState(boolean state) {
        this.state = state;
    }
    
    

}
