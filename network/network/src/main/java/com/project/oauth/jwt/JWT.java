package  com.project.oauth.jwt;
import  com.project.oauth.hashing.*;
import  com.project.oauth.jwt.expceptions.NoExpirationTime;
import  com.project.oauth.jwt.expceptions.NoIssuedAtTime;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class JWT{
	private String secretKey;
	public JWT(String secretKey) {
		this.secretKey = secretKey;
	}
	private String getHmacSignatureString(String algorithm,String signatureString) throws InvalidKeyException,NoSuchAlgorithmException {
		switch (algorithm) {
			case "HS256":{
				return HmacSha256.generateHashing(signatureString, secretKey.getBytes());
			}
			default:{
				throw new NoSuchAlgorithmException();
			}
		}
	}
	public String createToken(JWTHeader jwtHeader,JWTPayload jwtPayload) throws InvalidKeyException, NoSuchAlgorithmException, NoExpirationTime, NoIssuedAtTime {
		
		
		String jwtHeaderString = jwtHeader.createHeader();
		String jwtPayloadString = jwtPayload.createPayload();
	
		String jwtHeaderStringBase64 = new String(Base64.getUrlEncoder().encode(jwtHeaderString.getBytes())).split("=")[0];
		String jwtPayloadStringBase64 = new String(Base64.getUrlEncoder().encode(jwtPayloadString.getBytes())).split("=")[0];
		String signatureString = jwtHeaderStringBase64  + "." + jwtPayloadStringBase64;
		String hmacSignatureString = getHmacSignatureString(jwtHeader.getAlgorithm(), signatureString);
		
		return jwtHeaderStringBase64 +"."+jwtPayloadStringBase64+"."+hmacSignatureString;
	}
	public boolean verifyToken(String jwtToken) throws InvalidKeyException, NoSuchAlgorithmException, ParseException{
		String[] jwtComponents = jwtToken.split("[.]");
		if(jwtComponents.length != 3) return false;
		else {
			JSONParser jsonParser  = new JSONParser();
			JSONObject jsonJwtHeader = null;
			String jwtHeaderStringBase64  = jwtComponents[0];
			try {
				jsonJwtHeader = (JSONObject) jsonParser.parse(new String(Base64.getUrlDecoder().decode(jwtHeaderStringBase64)));
			}catch(Exception e){
				return false;
			}
			String jwtPayloadStringBase64  = jwtComponents[1];
			String signatureString = jwtHeaderStringBase64  + "." + jwtPayloadStringBase64;
			String hmacSignatureString = getHmacSignatureString((String) jsonJwtHeader.get("alg"), signatureString);

			if(jwtComponents[2].equals(hmacSignatureString)) {
				return true;
			}
			return false;
		}
	}
	public boolean hasTokenExpired(String jwtToken)  throws InvalidKeyException, NoSuchAlgorithmException, ParseException, NoExpirationTime{
		JSONParser jsonParser  = new JSONParser();
		String jwtPayloadStringBase64  = jwtToken.split("[.]")[1];
		JSONObject jsonJwtPayload = (JSONObject) jsonParser.parse(new String(Base64.getUrlDecoder().decode(jwtPayloadStringBase64)));
		Date nowDate = new Date();
		if(!jsonJwtPayload.containsKey("exp")) 
			throw new NoExpirationTime("No Expiration Time");
		Date expDate = new Date((long) jsonJwtPayload.get("exp"));
		System.out.println(nowDate);
		System.out.println(expDate);
		if(nowDate.compareTo(expDate) > 0) {
			return true;
		}
		return false;
	}
}