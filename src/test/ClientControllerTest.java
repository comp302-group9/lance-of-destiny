package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class ClientControllerTest {
    private ClientModel model;
    private ClientView view;
    private ClientController controller;
    private DataInputStream fromServer;
    private DataOutputStream toServer;
    private Socket socket;

    @BeforeEach
    public void setUp() throws IOException {
        model = mock(ClientModel.class);
        view = mock(ClientView.class);
        socket = mock(Socket.class);
        fromServer = mock(DataInputStream.class);
        toServer = mock(DataOutputStream.class);

        when(model.getSocket()).thenReturn(socket);
        when(model.getFromServer()).thenReturn(fromServer);
        when(model.getToServer()).thenReturn(toServer);

        controller = new ClientController(model, view);
    }

    @Test
    public void testHandleReadyButton() throws IOException {
        controller.handleReadyButton();
        verify(view).setReadyButtonEnabled(false);
        verify(toServer).writeUTF("READY");
        verify(view).setStatusLabelText("Waiting for other player...");
    }

    @Test
    public void testStartListening_AllReady() throws IOException, ClassNotFoundException {
        when(fromServer.readUTF()).thenReturn("ALL_READY");
        ObjectInputStream ois = mock(ObjectInputStream.class);
        when(socket.getInputStream()).thenReturn(ois);

        controller.startListening();

        verify(view).setStatusLabelText("All players are ready! Starting...");
        // further verifications related to switching mode can be added
    }
}

