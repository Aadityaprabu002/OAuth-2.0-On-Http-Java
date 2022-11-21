package project.oauth;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

enum JWTHashAlgorithm {
	HS256,
	RS256,
}

class JWTPayload{
	private Map<String, String> payload = null;
	public JWTPayload() {
		payload =  new LinkedHashMap<>();
	}
	
	/*
	 	* adds custom claim
	 */
	public void addCustomPayloadClaim(String claimName,String claimValue) {
		payload.put(claimName, claimValue);
	}
	
	/* 
	 	* sets issued at time 
	 */
	public void setIssuedAt() {
		if(!payload.containsKey("iat")) {
			Date date = new Date();
			payload.put("iat",String.valueOf(date.getTime()));
		}
	}
	
	/* 
		 * sets expiration time from current time 
		 * argument must be in minutes
	*/
	public void setExpirationTime(long expirationTime) {
		Date date = new Date();
		expirationTime = date.getTime() + (expirationTime * 60000);
		payload.put("exp", String.valueOf(expirationTime));
	}

	/*
	 	* sets issuer
	*/
	public void setIssuer(String issuer) {
		payload.put("iss", issuer);
	}
	
	/*  
	 	*sets subject
	 */
	public void setSubject(String subject) {
		payload.put("sub", subject);
	}
	public String createPayload() {
		String payloadString = payload.entrySet()
				.stream()
				.map(claim->"\""+ String.valueOf(claim.getKey())+"\":\""+String.valueOf(claim.getValue())+"\"")
				.collect(Collectors.joining(",")) +"}'" ;
		return payloadString;
	}
}
class JWTHeader {
	private Map<String, String> header = null;
	public JWTHeader() {
		header = new LinkedHashMap<>();
	}
	public void setHashAlgorithm(JWTHashAlgorithm jwtHashAlgorithm) 
	{
		switch (jwtHashAlgorithm) {
			case RS256:
				header.put("alg", "RS256");
				break;
			case HS256:
				header.put("alg", "HS256");
				break;
			default:
				header.put("alg", "HS256");
				break;
		}
		header.put("typ", "JWT");
		return;
		
	}
	public String createHeader() {
		String headerString = header.entrySet()
				.stream()
				.map(headerInfo->"\""+ String.valueOf(headerInfo.getKey())+"\":\""+String.valueOf(headerInfo.getValue())+"\"")
				.collect(Collectors.joining(",")) +"}'" ;
		return headerString;
	}
}
public class JWT{
	private JWTHashAlgorithm jwtHashAlgoritm;
	private String secretKey;
	public JWT(JWTHashAlgorithm jwtHashAlgoritm,String secretKey) {
		this.jwtHashAlgoritm = jwtHashAlgoritm;
		this.secretKey = secretKey;
	}
	public String createToken() {
		JWTHeader jwtHeader = new JWTHeader();
		jwtHeader.setHashAlgorithm(jwtHashAlgoritm);
		String headerString = jwtHeader.createHeader();
		
		JWTPayload jwtPayload = new JWTPayload();
		jwtPayload.setIssuedAt();
		jwtPayload.setExpirationTime(15);
		jwtPayload.setIssuer("");
		return "";
	}
}