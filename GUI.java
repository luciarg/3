/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientemensajeria;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author andres
 */
public class GUI extends JFrame implements ActionListener {

    private ClientProject cp;
    private JTextField portServer, hostServer, jNickname, message;
    private JButton start, stop, bSend;
    private JLabel tituloConfig, lPort, lHost, estadoConexion, lNickname, lChatImg, lTx, lRx;
    private JTextPane chatPane;
    private StyledDocument doc;
    private SimpleAttributeSet me, other;
    private ImageIcon iconConectado, iconDesconectado, chat;
    private DefaultTableModel tOnline;

    public GUI(ClientProject cp) {
        this.cp = cp;
        init();
    }

    private void init() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.add(getTabs(), BorderLayout.CENTER);
        this.setSize(1500, 600);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setResizable(false);
        this.setVisible(true);
    }

    //Genera las pestañas de navegación.
    private JTabbedPane getTabs() {

        JLabel lEstado = new JLabel("Estado");
        lEstado.setPreferredSize(new Dimension(200, 50));
        lEstado.setHorizontalTextPosition(SwingConstants.RIGHT);
        lEstado.setFont(lEstado.getFont().deriveFont(25f));

        JLabel lChat = new JLabel("Chat");
        lChat.setPreferredSize(new Dimension(200, 50));
        lChat.setHorizontalTextPosition(SwingConstants.RIGHT);
        lChat.setFont(lEstado.getFont().deriveFont(25f));

        UIManager.put("TabbedPane.selected", Color.decode("#E0F2F1"));
        UIManager.put("TabbedPane.borderHightlightColor", java.awt.Color.CYAN);
        UIManager.put("TabbedPane.darkShadow", java.awt.Color.CYAN);
        UIManager.put("TabbedPane.focus", Color.decode("#E0F2F1"));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);

        tabbedPane.addTab(null, getEstadoPanel());
        tabbedPane.addTab(null, initChat());

        tabbedPane.setTabComponentAt(0, lEstado);
        tabbedPane.setBackgroundAt(0, Color.decode("#80CBC4"));
        tabbedPane.setTabComponentAt(1, lChat);
        tabbedPane.setBackgroundAt(1, Color.decode("#80CBC4"));

        return tabbedPane;
    }

    private JPanel getEstadoPanel() {
        JPanel estado = new JPanel();
        estado.setBackground(Color.decode("#009688"));
        estado.setLayout(new GridBagLayout());

        Font fuenteTexto = new Font("HELVETICA", Font.BOLD, 20);

        initEstadoComponents(fuenteTexto);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1; //Esto es como ser redimensiona 
        c.weightx = 1;

        //Titulo
        c.weighty = 0.05;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 6;
        c.gridheight = 1;
        estado.add(this.tituloConfig, c);

        //LABELS 
        c.weighty = 0.01;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.lNickname, c);
        c.insets = new Insets(10, 5, 10, 5);
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.lPort, c);
        c.insets = new Insets(10, 5, 10, 5);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.lHost, c);

        //TextFields Nickname
        c.insets = new Insets(10, 5, 10, 5);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.jNickname, c);

        //TextFields HOST
        c.insets = new Insets(10, 5, 10, 5);
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.hostServer, c);

        //TextFields PORT
        c.insets = new Insets(10, 5, 10, 5);
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.portServer, c);

        //Estado
        c.weightx = 0.5;
        c.gridx = 3;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.estadoConexion, c
        );

        //Botoenes
        c.weightx = 1;
        c.weighty = 0.01;
        c.gridx = 4;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.start, c);

        c.insets = new Insets(10, 10, 10, 15);
        c.gridx = 5;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.stop, c);

        //Chat img
        c.weighty = 0.02;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 6;
        c.gridheight = 1;
        estado.add(this.lChatImg, c);

        return estado;

    }

    private void initEstadoComponents(Font fontLabel) {
        try {

            Font fuenteTitulo = new Font("HELVETICA", Font.BOLD, 25);

            this.tituloConfig = new JLabel("Estado del cliente");
            this.tituloConfig.setFont(fuenteTitulo);
            this.tituloConfig.setHorizontalAlignment(0);

            this.lNickname = new JLabel("Nickname");
            this.lNickname.setFont(fontLabel);
            this.lNickname.setHorizontalAlignment(0);

            this.lPort = new JLabel("Puerto");
            this.lPort.setFont(fontLabel);
            this.lPort.setHorizontalAlignment(0);

            this.lHost = new JLabel("Host");
            this.lHost.setFont(fontLabel);
            this.lHost.setHorizontalAlignment(0);

            BufferedImage imageConectado = ImageIO.read(new File("img/conectado.png"));
            this.iconConectado = new ImageIcon((Image) imageConectado);

            BufferedImage imageDesconectado = ImageIO.read(new File("img/desconectado.png"));
            this.iconDesconectado = new ImageIcon((Image) imageDesconectado);

            BufferedImage imageChat = ImageIO.read(new File("img/chat.png"));
            this.chat = new ImageIcon((Image) imageChat);

            this.estadoConexion = new JLabel();
            this.estadoConexion.setIcon(this.iconDesconectado);

            this.lChatImg = new JLabel();
            this.lChatImg.setIcon(this.chat);
            this.lChatImg.setHorizontalAlignment(0);

            this.portServer = new JTextField();
            this.portServer.setFont(fontLabel);
            this.portServer.setToolTipText("Puerto");

            this.hostServer = new JTextField();
            this.hostServer.setFont(fontLabel);
            this.hostServer.setToolTipText("Host");

            this.jNickname = new JTextField();
            this.jNickname.setFont(fontLabel);
            this.jNickname.setToolTipText("Nickname");

            this.start = new JButton("Start");
            this.start.setFont(fontLabel);
            this.start.addActionListener(this);
            this.start.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            this.stop = new JButton("Stop");
            this.stop.setFont(fontLabel);
            this.stop.addActionListener(this);
            this.stop.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            this.stop.setEnabled(false);

        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object boton = e.getSource();
        if (boton.equals(this.start)) {

        } else if (boton.equals(this.stop)) {

        }
        String str = e.getActionCommand();
        switch (str) {
            case "Start":
                if (checkPort(this.portServer) && checkHost(this.hostServer)
                        && checkNickname(this.jNickname)) {
                    this.cp.startOutServer(Integer.parseInt(this.portServer.getText()), this.hostServer.getText());
                    this.cp.setName(this.jNickname.getText());
                    this.jNickname.setEditable(false);
                    this.hostServer.setEditable(false);
                    this.portServer.setEditable(false);
                    this.start.setEnabled(false);
                    this.stop.setEnabled(true);
                    this.setTitle("Client of " + this.jNickname.getText()
                            + " connected to " + this.hostServer.getText()
                            + " " + this.portServer.getText());
                }
                break;
            case "Stop":
                this.cp.stopClient();
                break;
            case "Send":
                if (this.cp.onServer()) {
                    if (message.getText().length() > 0) {
                        addMsg("Me: " + message.getText(), 1);
                        cp.txMsg(message.getText());
                        message.setText("");
                        message.requestFocus();
                    }
                }
                break;
            default:
                System.err.println("Action NOT treated: " + e);
        }

    }
    //Añade una linea al chat.

    public void addMsg(String line, int opt) {
        try {
            String msg = line + "\n";
            if (opt == 1) {
                this.doc.insertString(this.doc.getLength(), msg, this.me);
            } else {
                this.doc.insertString(this.doc.getLength(), msg, this.other);
            }

        } catch (BadLocationException exc) {
        }
    }
    //Inicializa la ventana del chat.

    private JPanel initChat() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        Font fontLabel = new Font("HELVETICA", Font.BOLD, 20);
        JPanel panelChat = new JPanel(new GridBagLayout());
        JPanel panelSend = new JPanel(new GridBagLayout());
        JPanel panelOnline = new JPanel(new GridBagLayout());

        JLabel lChat = new JLabel("Chat");
        lChat.setFont(fontLabel);
        this.chatPane = new JTextPane();
        Font fontChat = new Font("HELVETICA", Font.BOLD, 20);
        this.chatPane.setFont(fontChat);
        DefaultCaret caret = (DefaultCaret) this.chatPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.chatPane.setEditable(false);

        this.doc = this.chatPane.getStyledDocument();

        this.me = new SimpleAttributeSet();
        StyleConstants.setForeground(this.me, Color.BLUE);
        this.other = new SimpleAttributeSet();
        StyleConstants.setForeground(this.other, Color.RED);

        JScrollPane pane = new JScrollPane(this.chatPane,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setPreferredSize(new Dimension(0, 450));

        JLabel lSend = new JLabel("Message");
        lSend.setFont(fontLabel);
        Font fontMessage = new Font("HELVETICA", Font.BOLD, 20);
        this.message = new JTextField(10);
        this.message.setFont(fontMessage);
        this.message.setEnabled(false);
        this.bSend = new JButton("Send");
        this.bSend.setFont(fontLabel);
        this.bSend.setEnabled(false);

        JLabel tTX = new JLabel("TX: ");
        tTX.setFont(fontLabel);
        this.lTx = new JLabel("0");
        this.lTx.setFont(fontLabel);

        JLabel tRX = new JLabel("RX: ");
        tRX.setFont(fontLabel);
        this.lRx = new JLabel("0");
        this.lRx.setFont(fontLabel);

        this.tOnline = new DefaultTableModel();
        this.tOnline.addColumn("Nickname", this.cp.getNicknames().toArray());

        JTable tableOnline = new JTable(this.tOnline);

        tableOnline.setEnabled(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        TableColumn column;
        for (int i = 0; i < this.tOnline.getColumnCount(); i++) {
            column = tableOnline.getColumnModel().getColumn(i);
            column.setPreferredWidth(200);
            column.setCellRenderer(centerRenderer);
        }
        tableOnline.setRowHeight(40);

        JScrollPane paneOnline = new JScrollPane(tableOnline,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        paneOnline.setPreferredSize(new Dimension(190, 400));

        GridBagConstraints gcChat = new GridBagConstraints();

        gcChat.fill = GridBagConstraints.HORIZONTAL;
        gcChat.weightx = 1;
        gcChat.gridx = 0;
        gcChat.gridy = 0;
        panelChat.add(lChat, gcChat);

        gcChat.gridx = 0;
        gcChat.gridy = 1;
        panelChat.add(pane, gcChat);

        GridBagConstraints gcOnline = new GridBagConstraints();
        gcOnline.fill = GridBagConstraints.HORIZONTAL;
        gcOnline.gridwidth = 1;
        gcOnline.gridx = 0;
        gcOnline.gridy = 0;
        panelOnline.add(tTX, gcOnline);

        gcOnline.gridx = 1;
        gcOnline.gridy = 0;
        panelOnline.add(this.lTx, gcOnline);

        gcOnline.gridx = 2;
        gcOnline.gridy = 0;
        panelOnline.add(tRX, gcOnline);

        gcOnline.gridx = 3;
        gcOnline.gridy = 0;
        panelOnline.add(this.lRx, gcOnline);
        gcOnline.gridwidth = 4;
        gcOnline.weighty = 1;
        gcOnline.gridx = 0;
        gcOnline.gridy = 1;

        panelOnline.add(paneOnline, gcOnline);

        GridBagConstraints gcSend = new GridBagConstraints();
        gcSend.fill = GridBagConstraints.HORIZONTAL;
        gcSend.weightx = 0.5;
        gcSend.gridx = 0;
        gcSend.gridy = 0;
        gcSend.ipadx = 10;
        panelSend.add(lSend, gcSend);
        gcSend.weightx = 1;
        gcSend.gridx = 1;
        gcSend.gridy = 0;
        panelSend.add(message, gcSend);

        gcSend.gridx = 1;
        gcSend.gridy = 1;
        panelSend.add(this.bSend, gcSend);

        this.bSend.addActionListener(this);

        panel.add(panelChat, BorderLayout.CENTER);
        panel.add(panelSend, BorderLayout.SOUTH);
        panel.add(panelOnline, BorderLayout.EAST);

        return panel;
    }

    //Comprueba que el puerto sea un Integer y que cumpla una
    // longitud determinada.
    private boolean checkPort(JTextField port) {
        int i;
        try {
            i = Integer.parseInt(port.getText());
            return port.getText().length() > 0 && port.getText().length() < 6;
        } catch (NumberFormatException e) {
            port.requestFocus();
            JOptionPane.showMessageDialog(null, "Puerto no valido o vacío.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }

    //Comprueba que el host tenga una longitud determinada.
    private boolean checkHost(JTextField host) {
        if (host.getText().length() > 0) {
            return true;
        } else {
            host.requestFocus();
            JOptionPane.showMessageDialog(null, "Host no valido o vacío.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    //Comprueba que el nickname tenga una longitud determinada.
    private boolean checkNickname(JTextField nickname) {
        if (nickname.getText().length() > 0) {
            return true;
        } else {
            nickname.requestFocus();
            JOptionPane.showMessageDialog(null, "Nickname no valido o vacío.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    //Setea el estado a conectado.
    public void conectado() {
        this.estadoConexion.setIcon(this.iconConectado);
        this.bSend.setEnabled(true);
        this.message.setEnabled(true);
    }

    //Cambiar el estado a parado.
    public void desconectado() {
        this.estadoConexion.setIcon(this.iconDesconectado);
        this.bSend.setEnabled(false);
        this.message.setEnabled(false);
    }

    //Setea los estados de los botones y textFilds a STOP
    public void stopServer() {
        this.jNickname.setEditable(true);
        this.hostServer.setEditable(true);
        this.portServer.setEditable(true);
        this.start.setEnabled(true);
        this.stop.setEnabled(false);
    }

    public void refresh() {
        this.tOnline.setRowCount(0);
        Object rowData[] = new Object[1];
        for (String s : this.cp.getNicknames()) {
            rowData[0] = s;
            this.tOnline.addRow(rowData);
        }
        this.tOnline.fireTableDataChanged();
    }

    public JLabel getlTx() {
        return lTx;
    }


    public JLabel getlRx() {
        return lRx;
    }

    
    
}
