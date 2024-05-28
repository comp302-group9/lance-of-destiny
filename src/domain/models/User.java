package domain.models;

public class User {
	
	private String username;
    private String password;
    private String email;
    
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

	public String getEmail() {
		return null;
	}

}
