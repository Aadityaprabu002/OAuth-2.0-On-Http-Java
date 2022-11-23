package project.app;
import java.util.Date;

import project.oauth.jwt.*;

public final class App {
	public static void main(String[] args) {
		JWT jwt = new JWT("123456");
		JWTHeader jwtHeader = new JWTHeader();
		JWTPayload jwtPayload = new JWTPayload();
		jwtHeader.setHashAlgorithm(JWTHashAlgorithm.HS256);
		jwtPayload.setSubject("1234567890");
		jwtPayload.addCustomPayloadClaim("name","John Doe");
		jwtPayload.setIssuedAt(new Date());
		jwtPayload.setExpirationTime(15);
		try {
			String jwtTokenString = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiZXhwIjoxNjY5MjI3NTY1MDAzLCJpYXQiOjE2NjkyMjY2NjUwMDN9.2c6757788a96f4b988a372d9dec14234bd66d10a8970e624c26985d61cf5f16a";//jwt.createToken(jwtHeader, jwtPayload);
			
			System.out.println("Verified used ?:"+ jwt.verifyToken(jwtTokenString));
			System.out.println("Has Expired ? :"+ jwt.hasTokenExpired(jwtTokenString));
		}
		
		catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
}
