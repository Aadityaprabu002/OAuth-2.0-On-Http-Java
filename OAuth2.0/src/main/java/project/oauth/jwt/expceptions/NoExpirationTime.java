package project.oauth.jwt.expceptions;

public class NoExpirationTime extends Exception {
	public NoExpirationTime(String message) {
		super(message);
	}
}
