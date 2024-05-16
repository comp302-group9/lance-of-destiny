package src.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {
    private Client client;
    private Server server;
    private static final int PORT = 12345;

    @BeforeEach
    public void setUp() throws IOException {
        server = new Server(PORT);
        new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        
        client = new Client("localhost", PORT);
        client.connect();
    }

    @AfterEach
    public void tearDown() throws IOException {
        client.close();
        server.stop();
    }

    @Test
    public void testSendMessage() throws IOException {
        client.sendMessage("Hello, Server!");
        String response = server.receiveMessage();
        assertEquals("Hello, Server!", response);
    }

    @Test
    public void testReceiveMessage() throws IOException {
        server.sendMessage("Hello, Client!");
        String response = client.receiveMessage();
        assertEquals("Hello, Client!", response);
    }

    @Test
    public void testConnection() {
        assertNotNull(client);
        assertNotNull(server);
    }
}

