package network;

import java.awt.Color;
import java.io.*;
import java.net.*;

import javax.swing.JPanel;

import domain.models.RunningModeModel;
import ui.screens.RModeUI.GameStatusPanel;

public class ClientHandler implements Runnable {
    private Socket socket;
    private boolean ready;
    private PrintWriter out;
    private BufferedReader in;
    private ServerController controller;
    private GameStatusPanel gsp;
    private RunningModeModel rmodel;

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
                else if (message.startsWith("B")) {
                    String numberPart = message.substring(1); // Extract the part of the message after "S"
                    try {
                        int number = Integer.parseInt(numberPart); // Convert the extracted part to an integer
                        gsp.setBarriersLeft(number);
                    } catch (NumberFormatException e) {}
                }

                else if (message.startsWith("L")) {
                    String numberPart = message.substring(1); // Extract the part of the message after "S"
                    try {
                        int number = Integer.parseInt(numberPart); // Convert the extracted part to an integer
                        gsp.setLives(number);
                    } catch (NumberFormatException e) {}
                }
                else if (message.equals("Ymir1")) {
                    rmodel.badspells.get(0).increase();
                    rmodel.badspells.get(0).Activate();
                    rmodel.badspells.get(0).startTimer();
                }else if (message.equals("Ymir2")) {
                    rmodel.badspells.get(1).increase();
                    rmodel.badspells.get(1).Activate();
                    rmodel.badspells.get(1).startTimer();
                }else if (message.equals("Ymir3")) {
                    rmodel.badspells.get(2).increase();
                    rmodel.badspells.get(2).Activate();
                    rmodel.badspells.get(2).startTimer();
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

    public void setModel(RunningModeModel m){
        this.rmodel=m;
    }
}
