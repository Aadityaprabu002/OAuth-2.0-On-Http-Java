package project.oauth.jwt.expceptions;

public class NoIssuedAtTime extends Exception{
	public NoIssuedAtTime(String message) {
		super(message);
	}
}
