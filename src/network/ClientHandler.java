package network;

import java.awt.Color;
import java.io.*;
import java.net.*;

import javax.swing.JPanel;

import ui.screens.RModeUI.GameStatusPanel;

public class ClientHandler implements Runnable {
    private Socket socket;
    private boolean ready;
    private PrintWriter out;
    private BufferedReader in;
    private ServerController controller;
    private GameStatusPanel gsp;

    public ClientHandler(Socket socket, ServerController controller) {
        this.socket = socket;
        this.controller = controller;
        this.ready = false;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            String clientName = in.readLine();
            controller.addClientName(clientName); // Add client's name to the right panel

            while (true) {
                String message = in.readLine();
                if (message == null) {
                    break;
                } else if (message.equals("READY")) {
                    ready = true;
                    controller.setClientReadyStatus("Ready");
                    controller.checkIfAllReady();
                }else if (message.startsWith("S")) {
                    String numberPart = message.substring(1); // Extract the part of the message after "S"
                    try {
                        int number = Integer.parseInt(numberPart); // Convert the extracted part to an integer
                        gsp.setScore(number);
                    } catch (NumberFormatException e) {}
                }
                //Mesajlar buradan alınıyor Lives ve barrier number eklenecek
                //Task 1
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean isReady() {
        return ready;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void sendGrid(int[][] grid) {
        try {
            ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
            objectOut.writeObject(grid);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void cleanUp() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setGSP(GameStatusPanel g) {
        this.gsp=g;
    }
}
