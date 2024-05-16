package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class ServerControllerTest {
    private ServerModel model;
    private ServerView view;
    private ServerController controller;
    private DataInputStream fromClient;
    private DataOutputStream toClient;
    private Socket clientSocket;
    private ServerSocket serverSocket;

    @BeforeEach
    public void setUp() throws IOException {
        model = mock(ServerModel.class);
        view = mock(ServerView.class);
        clientSocket = mock(Socket.class);
        serverSocket = mock(ServerSocket.class);
        fromClient = mock(DataInputStream.class);
        toClient = mock(DataOutputStream.class);

        when(model.getServerSocket()).thenReturn(serverSocket);
        when(model.getClientSocket(0)).thenReturn(clientSocket);
        when(model.getFromClients()[0]).thenReturn(fromClient);
        when(model.getToClients()[0]).thenReturn(toClient);

        controller = new ServerController(model, view);
    }

    @Test
    public void testHandleSelfReady() throws IOException {
        controller.handleSelfReady();
        verify(view).setReadyButtonEnabled(false);
        verify(view).setStatusLabelText("Server Ready");
        verify(model).incrementReadyCount();
    }

    @Test
    public void testNotifyAllPlayersReady() throws IOException {
        controller.notifyAllPlayersReady();
        verify(toClient).writeUTF("ALL_READY");
        verify(toClient).flush();
        // further verifications related to switching mode can be added
    }

    @Test
    public void testHandleClient_ReadyMessage() throws IOException {
        when(fromClient.readUTF()).thenReturn("READY");

        controller.handleClient(0);

        verify(model).incrementReadyCount();
    }
}

