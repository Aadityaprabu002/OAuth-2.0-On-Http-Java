package com.project.network;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.network.models.User;
import com.project.network.models.Token;
import com.project.oauth.jwt.JWT;
import com.project.oauth.jwt.JWTHashAlgorithm;
import com.project.oauth.jwt.JWTHeader;
import com.project.oauth.jwt.JWTPayload;
import com.project.oauth.jwt.expceptions.NoExpirationTime;







@RestController
public class AuthenticatedServiceProvider {
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST,value = "/login")
	public String login(@RequestBody User user ) {
		
		user.getEmail();
		JWT jwt = new JWT("SECRET-KEY");
		JWTHeader jwtHeader = new JWTHeader();
		JWTPayload jwtPayload = new JWTPayload();
		jwtHeader.setHashAlgorithm(JWTHashAlgorithm.HS256);
		jwtPayload.setSubject("12345");
		jwtPayload.addCustomPayloadClaim("email",user.getEmail());
		jwtPayload.setIssuedAt(new Date());
		jwtPayload.setExpirationTime(1);
		String jwtTokenString = "";
		try {
			jwtTokenString = jwt.createToken(jwtHeader, jwtPayload); 
		}
		catch (Exception e) {
			System.out.println(e);
		}
		JSONObject response = new JSONObject();
		response.put("Status",1 );
		response.put("Token",jwtTokenString);
		return response.toJSONString();
		
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST,value = "/verify")
	public String verify(@RequestBody Token token) {
		JSONObject response = new JSONObject();
		response.put("Status", 0);
		response.put("Message","Invalid token");
		String tokenString = token.getToken();
		JWT jwt = new JWT("SECRET-KEY");
		try {
			if(jwt.verifyToken(tokenString) && !jwt.hasTokenExpired(tokenString)) {
				response.put("Status", 1);
				response.put("Message","Valid token");
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException | ParseException | NoExpirationTime e) {
			e.printStackTrace();
		}
		System.out.print(response);
		return response.toJSONString();
		
	}
}
