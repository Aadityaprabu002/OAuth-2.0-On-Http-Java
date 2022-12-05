package  com.project.oauth.jwt.expceptions;

public class NoExpirationTime extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoExpirationTime(String message) {
		super(message);
	}
}
