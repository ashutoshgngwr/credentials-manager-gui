package ashutoshgangwar.credentialsmanager;

public class CredentialEntry {
	
	private String url, username, password;
	
	protected CredentialEntry(String url, String username, String password) {
		this.setUrl(url);
		this.setUsername(username);
		this.setPassword(password);
	}

	protected String getUrl() {
		return url;
	}

	protected void setUrl(String url) {
		this.url = url;
	}

	protected String getUsername() {
		return username;
	}

	protected void setUsername(String username) {
		this.username = username;
	}

	protected String getPassword() {
		return password;
	}

	protected void setPassword(String password) {
		this.password = password;
	}
}
