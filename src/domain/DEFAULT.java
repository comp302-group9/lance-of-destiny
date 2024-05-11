package domain;

public class DEFAULT {
	public static final int screenHeight = 600;
	public static final int screenWidth = screenHeight * 16/9;

    public static final int ROWS = 11;
    public static final int COLUMNS = 19;

    public static int barrierWidth = 51;
	public static int barrierHeight =15;

    public static int paddleWidth = screenWidth/10;
    public static int paddleHeight = 20;

    public void setPaddleWidth(int w){
        paddleWidth=w;
    }
}
