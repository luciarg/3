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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author andres
 */
public class Window implements ActionListener {

    private ClientProject cp;
    private JTextField textPortOS, textHostOS, textNickName, message;
    private JButton start, bSend;
    private JFrame f;
    private Box center;
    private JTextPane chat;
    private StyledDocument doc;
    private SimpleAttributeSet me, other;

    public Window(ClientProject cp) {
        this.cp = cp;
        getData();
    }

    //Añade una linea al chat.
    public void addMsg(String line, int opt) {
        try {
            String msg = line + "\n";
            if (opt == 1) {
                doc.insertString(doc.getLength(), msg, me);
            } else {
                doc.insertString(doc.getLength(), msg, other);
            }

        } catch (BadLocationException exc) {
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        switch (str) {
            case "Start":

                if (checkPort(textPortOS.getText())) {
                    if (checkLenght(textHostOS.getText())) {
                        if (checkLenght(textNickName.getText())) {
                            cp.startOutServer(Integer.parseInt(textPortOS.getText()), textHostOS.getText());
                            cp.setName(textNickName.getText());
                            textPortOS.setEnabled(false);
                            textHostOS.setEnabled(false);
                            start.setEnabled(false);
                            if (waitServerConnection()) {
                                f.remove(center);
                                initChat();
                            } else {
                                JOptionPane.showMessageDialog(null, "The server does not respond.",
                                        "Error Server", JOptionPane.WARNING_MESSAGE);
                                textPortOS.setEnabled(true);
                                textHostOS.setEnabled(true);
                                start.setEnabled(true);
                                textHostOS.requestFocus();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Enter a valid NickName.",
                                    "Error Host", JOptionPane.ERROR_MESSAGE);
                            textNickName.requestFocus();
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Enter a valid host.",
                                "Error Host", JOptionPane.ERROR_MESSAGE);
                        textHostOS.requestFocus();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Enter a valid port.",
                            "Error Port", JOptionPane.ERROR_MESSAGE);
                    textPortOS.requestFocus();
                }

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

    private boolean checkPort(String port) {
        int i;
        try {
            i = Integer.parseInt(port);
            return checkLenght(port) && port.length() < 6;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    private boolean checkLenght(String st) {
        return st.length() > 0;

    }

    //Inicializa ventana de inicializacion de parametros.
    private void getData() {
        f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 200);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation(dim.width / 2 - f.getSize().width / 2, dim.height / 2 - f.getSize().height / 2);

        center = Box.createVerticalBox();

        Font fontLabel = new Font("HELVETICA", Font.BOLD, 20);

        textPortOS = new JTextField();
        textPortOS.setFont(fontLabel);
        textHostOS = new JTextField();
        textHostOS.setFont(fontLabel);
        textNickName = new JTextField();
        textNickName.setFont(fontLabel);

        Box bPortOS = Box.createHorizontalBox();
        JLabel lPortOS = new JLabel("Port Out Server ");
        lPortOS.setFont(fontLabel);
        bPortOS.add(lPortOS);
        bPortOS.add(textPortOS);
        Box bHostOS = Box.createHorizontalBox();
        JLabel lHostOS = new JLabel("Host Out Server ");
        lHostOS.setFont(fontLabel);
        bHostOS.add(lHostOS);
        bHostOS.add(textHostOS);
        Box bNickName = Box.createHorizontalBox();
        JLabel lNickName = new JLabel("NickName ");
        lNickName.setFont(fontLabel);
        bNickName.add(lNickName);
        bNickName.add(textNickName);

        start = new JButton("Start");
        start.setFont(fontLabel);
        start.addActionListener(this);
        start.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        center.add(bPortOS);
        center.add(bHostOS);
        center.add(bNickName);
        center.add(start);

        f.add(center, BorderLayout.CENTER);
        f.getRootPane().setDefaultButton(start);
        f.setResizable(false);
        f.setVisible(true);
    }

    //Inicializa la ventana del chat.
    private void initChat() {

        Font fontLabel = new Font("HELVETICA", Font.BOLD, 20);
        JPanel panelChat = new JPanel(new GridBagLayout());
        JPanel panelSend = new JPanel(new GridBagLayout());
        JLabel lChat = new JLabel("Chat");
        lChat.setFont(fontLabel);
        chat = new JTextPane();
        Font fontChat = new Font("HELVETICA", Font.BOLD, 20);
        chat.setFont(fontChat);
        DefaultCaret caret = (DefaultCaret) chat.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        chat.setEditable(false);

        this.doc = chat.getStyledDocument();

        me = new SimpleAttributeSet();
        StyleConstants.setForeground(me, Color.BLUE);
        other = new SimpleAttributeSet();
        StyleConstants.setForeground(other, Color.RED);

        JScrollPane pane = new JScrollPane(chat,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setPreferredSize(new Dimension(950, 545));
        pane.setMinimumSize(new Dimension(10, 10));
        JLabel lSend = new JLabel("Message");
        lSend.setFont(fontLabel);
        Font fontMessage = new Font("HELVETICA", Font.BOLD, 20);
        message = new JTextField(10);
        message.setFont(fontMessage);
        bSend = new JButton("Send");
        bSend.setFont(fontLabel);

        GridBagConstraints gcChat = new GridBagConstraints();

        gcChat.fill = GridBagConstraints.HORIZONTAL;
        gcChat.weightx = 1;
        gcChat.gridx = 0;
        gcChat.gridy = 0;
        panelChat.add(lChat, gcChat);

        gcChat.gridx = 0;
        gcChat.gridy = 1;
        panelChat.add(pane, gcChat);

        GridBagConstraints gcSend = new GridBagConstraints();
        gcSend.fill = GridBagConstraints.HORIZONTAL;
        gcSend.weightx = 1;
        gcSend.gridx = 0;
        gcSend.gridy = 0;
        gcSend.ipadx = 10;
        panelSend.add(lSend, gcSend);

        gcSend.gridx = 1;
        gcSend.gridy = 0;
        panelSend.add(message, gcSend);

        gcSend.gridx = 1;
        gcSend.gridy = 1;
        panelSend.add(bSend, gcSend);

        bSend.addActionListener(this);

        f.add(panelChat, BorderLayout.CENTER);
        f.add(panelSend, BorderLayout.SOUTH);

        f.getRootPane().setDefaultButton(bSend);
        f.setTitle("Chat from " + cp.getName() + " server " + cp.getNameServer());
        f.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation(dim.width / 2 - f.getSize().width / 2, dim.height / 2 - f.getSize().height / 2);
        f.setResizable(false);
        f.setVisible(true);
        message.requestFocus();
    }

    private boolean waitServerConnection() {
        int aux = 0;
        while (!cp.onServer()) {
            try {
                if (aux <= 5) {
                    aux++;
                    Thread.sleep(1000);
                } else {
                    return false;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }

}
