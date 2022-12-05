package  com.project.oauth.jwt.expceptions;

public class NoIssuedAtTime extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoIssuedAtTime(String message) {
		super(message);
	}
}
