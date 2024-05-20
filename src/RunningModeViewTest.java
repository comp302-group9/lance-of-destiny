import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import static org.mockito.Mockito.*;

public class RunningModeViewTest {
    private RunningModeModel mockModel;
    private RunningModeView runningModeView;
    private Graphics mockGraphics;
    private BufferedImage mockBackgroundImage;
    private BufferedImage mockHeartImage;

    @BeforeEach
    public void setUp() {
        mockModel = mock(RunningModeModel.class);
        runningModeView = new RunningModeView(mockModel);
        mockGraphics = mock(Graphics.class);

        mockBackgroundImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        mockHeartImage = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);

        runningModeView.backgroundImage = mockBackgroundImage;
        runningModeView.heartImage = mockHeartImage;
    }

    @Test
    public void testPaintComponent_GameNotOver_MultipleGameObjects() {
        when(mockModel.isGameOver()).thenReturn(false);
        Paddle mockPaddle = mock(Paddle.class);
        Fireball mockFireball = mock(Fireball.class);
        when(mockModel.getPaddle()).thenReturn(mockPaddle);
        when(mockModel.getFireball()).thenReturn(mockFireball);
        RunningModeModel.boxes = new domain.objects.Box[]{mock(domain.objects.Box.class)};
        RunningModeModel.barriers = new Barrier[]{mock(Barrier.class)};
        when(mockModel.getChances()).thenReturn(3);

        runningModeView.paintComponent(mockGraphics);

        verify(mockGraphics).drawImage(eq(mockBackgroundImage), eq(0), eq(0), eq(800), eq(600), eq(runningModeView));
        verify(mockPaddle).draw(mockGraphics);
        verify(mockFireball).draw(mockGraphics);
        verify(RunningModeModel.boxes[0]).draw(mockGraphics);
        verify(RunningModeModel.barriers[0]).draw(mockGraphics);
        verify(mockGraphics).drawString(eq("Lives:"), eq(650), eq(30));
        for (int i = 0; i < 3; i++) {
            verify(mockGraphics).drawImage(eq(mockHeartImage), eq(720 + i * 25), eq(10), eq(20), eq(20), eq(runningModeView));
        }
    }

    @Test
    public void testPaintComponent_GameOver() {
        when(mockModel.isGameOver()).thenReturn(true);
        when(mockModel.gameOverMessage()).thenReturn("Game Over");

        runningModeView.paintComponent(mockGraphics);

        verify(mockGraphics).drawImage(eq(mockBackgroundImage), eq(0), eq(0), eq(800), eq(600), eq(runningModeView));
        verify(mockGraphics).drawString(eq("Game Over"), anyInt(), anyInt());
        verify(mockGraphics, never()).drawImage(eq(mockHeartImage), anyInt(), anyInt(), anyInt(), anyInt(), eq(runningModeView));
    }

    @Test
    public void testPaintComponent_ZeroLives() {
        when(mockModel.isGameOver()).thenReturn(false);
        when(mockModel.getPaddle()).thenReturn(mock(Paddle.class));
        when(mockModel.getFireball()).thenReturn(mock(Fireball.class));
        RunningModeModel.boxes = new domain.objects.Box[]{mock(domain.objects.Box.class)};
        RunningModeModel.barriers = new Barrier[]{mock(Barrier.class)};
        when(mockModel.getChances()).thenReturn(0);

        runningModeView.paintComponent(mockGraphics);

        verify(mockGraphics).drawImage(eq(mockBackgroundImage), eq(0), eq(0), eq(800), eq(600), eq(runningModeView));
        verify(mockGraphics).drawString(eq("Lives:"), eq(650), eq(30));
        verify(mockGraphics, never()).drawImage(eq(mockHeartImage), anyInt(), anyInt(), anyInt(), anyInt(), eq(runningModeView));
    }

    @Test
    public void testPaintComponent_VariousLives() {
        when(mockModel.isGameOver()).thenReturn(false);
        when(mockModel.getPaddle()).thenReturn(mock(Paddle.class));
        when(mockModel.getFireball()).thenReturn(mock(Fireball.class));
        RunningModeModel.boxes = new domain.objects.Box[]{mock(domain.objects.Box.class)};
        RunningModeModel.barriers = new Barrier[]{mock(Barrier.class)};

        for (int lives = 1; lives <= 3; lives++) {
            when(mockModel.getChances()).thenReturn(lives);

            runningModeView.paintComponent(mockGraphics);

            verify(mockGraphics).drawImage(eq(mockBackgroundImage), eq(0), eq(0), eq(800), eq(600), eq(runningModeView));
            verify(mockGraphics).drawString(eq("Lives:"), eq(650), eq(30));
            for (int i = 0; i < lives; i++) {
                verify(mockGraphics).drawImage(eq(mockHeartImage), eq(720 + i * 25), eq(10), eq(20), eq(20), eq(runningModeView));
            }
        }
    }

    @Test
    public void testPaintComponent_NoBoxesNoBarriers() {
        when(mockModel.isGameOver()).thenReturn(false);
        when(mockModel.getPaddle()).thenReturn(mock(Paddle.class));
        when(mockModel.getFireball()).thenReturn(mock(Fireball.class));
        RunningModeModel.boxes = new domain.objects.Box[0];
        RunningModeModel.barriers = new Barrier[0];
        when(mockModel.getChances()).thenReturn(3);

        runningModeView.paintComponent(mockGraphics);

        verify(mockGraphics).drawImage(eq(mockBackgroundImage), eq(0), eq(0), eq(800), eq(600), eq(runningModeView));
        verify(mockPaddle).draw(mockGraphics);
        verify(mockFireball).draw(mockGraphics);
        verify(mockGraphics, never()).drawImage(eq(mockHeartImage), anyInt(), anyInt(), anyInt(), anyInt(), eq(runningModeView));
    }
}
