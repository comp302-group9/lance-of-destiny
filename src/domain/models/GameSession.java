package domain.models;

public class GameSession {

	private int gameId;
	private User user;
	private int life;
	private int score;
	private String grid;
	
	public GameSession(User user) {
		this.user = user;
		this.setLife(3);
		this.setScore(0);
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getGameId() {
		return gameId;
	}

	public User getUser() {
		return user;
	}

	public int getLife() {
		return life;
	}

	public int getScore() {
		return score;
	}
	
	
}
