/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientemensajeria;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author andres
 */
public class ServerClient implements Runnable {

    private ClientProject cp;
    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    public ServerClient(ClientProject cp, Socket socket) {
        try {
            this.cp = cp;
            this.socket = socket;
            in = new Scanner(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        try {
            processMessages();
            socket.close();
            System.out.println("Server disconnecect.");
            cp.disconnectServer();

        } catch (IOException ex) {
            Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Recibe los mensajes, en caso de desconexion muestra un mensaje por pantalla.
    protected void processMessages() throws IOException {
        while (in.hasNextLine()) {
            String line = in.nextLine();

            switch (line.trim().toUpperCase().substring(0, 7)) {
                case "$MENSAJ":
                    cp.showMsj(line.substring(8, line.length()));
                    break;

                case "$SERADD":
                    List<Integer> positionsAddServer = new LinkedList<>();
                    int positionAddServer = line.indexOf("$", 0);

                    while (positionAddServer != -1) {
                        positionsAddServer.add(positionAddServer);
                        positionAddServer = line.indexOf("$", positionAddServer + 1);
                    }
                    for (int i = 2; i <= positionsAddServer.size() - 1; i += 2) {
                        this.cp.addNickname(line.substring(positionsAddServer.get(i) + 1, positionsAddServer.get(i + 1)));
                    }
                    this.cp.refreshNicks();
                    break;
                case "$SERREM":
                    List<Integer> positionsRemServer = new LinkedList<>();
                    int positionRemServer = line.indexOf("$", 0);

                    while (positionRemServer != -1) {
                        positionsRemServer.add(positionRemServer);
                        positionRemServer = line.indexOf("$", positionRemServer + 1);
                    }
                    for (int i = 2; i <= positionsRemServer.size() - 1; i += 2) {

                        this.cp.remNickname(line.substring(positionsRemServer.get(i) + 1, positionsRemServer.get(i + 1)));
                    }
                    this.cp.refreshNicks();
                    break;
                case "$CLIADD":
                    List<Integer> positionsAddClient = new LinkedList<>();
                    int positionAddClient = line.indexOf("$", 0);

                    while (positionAddClient != -1) {
                        positionsAddClient.add(positionAddClient);
                        positionAddClient = line.indexOf("$", positionAddClient + 1);
                    }

                    this.cp.addNickname(line.substring(positionsAddClient.get(2) + 1, positionsAddClient.get(2 + 1)));
                    this.cp.refreshNicks();
                    break;
                case "$CLIREM":
                    List<Integer> positionsRemClient = new LinkedList<>();
                    int positionRemClient = line.indexOf("$", 0);

                    while (positionRemClient != -1) {
                        positionsRemClient.add(positionRemClient);
                        positionRemClient = line.indexOf("$", positionRemClient + 1);
                    }
                    this.cp.remNickname(line.substring(positionsRemClient.get(2) + 1, positionsRemClient.get(2 + 1)));
                    this.cp.refreshNicks();
                    break;
                case "$CANCEL":
                    JOptionPane.showMessageDialog(null, "Nickname ya usado.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    this.cp.stopClient();
                    break;
            }

        }
    }

    //Envia el mensaje.
    public void txMsg(String msg) {
        out.println("$MENSAJ$" + msg);
    }

    public Socket getSocket() {
        return socket;
    }

}
