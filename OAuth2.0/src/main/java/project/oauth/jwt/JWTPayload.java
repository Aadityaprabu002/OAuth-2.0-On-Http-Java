package project.oauth.jwt;
import org.json.simple.*;

import project.oauth.jwt.expceptions.NoExpirationTime;
import project.oauth.jwt.expceptions.NoIssuedAtTime;

import java.util.Date;

public class JWTPayload{
	private JSONObject payload = null;
	public JWTPayload() {
		payload =  new JSONObject();
	}
	
	/*
	 	* adds custom claim
	 */
	@SuppressWarnings("unchecked")
	public <T> void addCustomPayloadClaim(String claimName,T claimValue) {
		payload.put(claimName, claimValue);
	
	}
	
	/* 
	 	* sets issued at time 
	 */
	
	@SuppressWarnings("unchecked")
	public void setIssuedAt(Date date) {
		if(!payload.containsKey("iat")) {
			payload.put("iat",date.getTime());
		
		}
	}
	
	/* 
		 * sets expiration time from current time 
		 * argument must be in minutes
	*/
	
	@SuppressWarnings("unchecked")
	public void setExpirationTime(long expirationTime) {
		Date date = new Date();
		expirationTime = date.getTime() + (expirationTime * 60000);
		payload.put("exp", expirationTime );

	}

	/*
	 	* sets issuer
	*/

	@SuppressWarnings("unchecked")
	public void setIssuer(String issuer) {
		payload.put("iss", issuer);
	
	}
	
	/*  
	 	*sets subject
	 */
	@SuppressWarnings("unchecked")
	public void setSubject(String subject) {
		payload.put("sub", subject);
	
	}
    String createPayload() throws NoExpirationTime, NoIssuedAtTime {
    	if(!payload.containsKey("exp"))
    		throw new NoExpirationTime("No Expiration Time");
    	if(!payload.containsKey("iat"))
    		throw new NoIssuedAtTime("No Issued At Time");
    	return payload.toJSONString();
	}
}