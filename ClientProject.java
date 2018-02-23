/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientemensajeria;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author andres
 */
public class ClientProject {

    private OutServer os;
    private ServerClient sc;
    private Window w;
    private GUI gui;
    private String name;
    private String nameServer;
    private ArrayList<String> nicknames;

    public ClientProject() {
        this.nicknames = new ArrayList<>();
        this.gui = new GUI(this);
        //this.w = new Window(this);
    }

    //Añade un servidor si no hay uno e inicia su hilo de ejecución.
    public void addServer(Socket s) {
        if (this.sc == null) {
            nameServer = s.getInetAddress().toString() + " port " + s.getPort();
            this.sc = new ServerClient(this, s);
            new Thread(this.sc).start();
            System.out.println("Server connected...");
            this.gui.conectado();
        }
    }

    public void addNickname(String nick) {
        this.nicknames.add(nick);
        this.gui.refresh();
    }

    public void remNickname(String nick) {
        this.nicknames.remove(nick);
        this.gui.refresh();
    }

    //Elimina el servidor.
    public void disconnectServer() {
        this.nicknames.clear();
        this.refreshNicks();
        this.sc = null;
        this.gui.desconectado();
    }

    public void stopClient() {
        this.os.setState(false);
        this.os = null;
        if (this.sc != null) {
            try {
                this.sc.getSocket().close();
            } catch (IOException ex) {

            }
            this.sc = null;
        }
        this.name = "";
        this.nameServer = "";
        this.gui.stopServer();
    }

    //Comprueba si existe un elemento servidor.
    public boolean onServer() {
        return this.sc != null;
    }

    //Cambia el nombre.
    public void setName(String name) {
        this.name = name;
    }

    //Muestra el mensaje.
    public void showMsj(String line) {
        this.gui.addMsg(line, 2);
        int rx = Integer.parseInt(this.gui.getlRx().getText())+1;
        this.gui.getlRx().setText(Integer.toString(rx));
    }

    //Inicia el realizador de conexiones.
    public void startOutServer(int port, String ip) {
        if (this.os == null) {
            this.os = new OutServer(this, port, ip);
            this.os.start();
        }
    }

    //Envia el mensaje.
    public void txMsg(String msg) {
        this.sc.txMsg(msg);
        int tx = Integer.parseInt(this.gui.getlTx().getText())+1;
        this.gui.getlTx().setText(Integer.toString(tx));
    }

    //Obtiene el nombre.
    public String getName() {
        return this.name;
    }

    //Obtiene el nombre del server.
    public String getNameServer() {
        return this.nameServer;
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }

    public void clearNicknames() {
        this.nicknames.clear();
    }

    public void refreshNicks() {
        this.gui.refresh();
    }

}
