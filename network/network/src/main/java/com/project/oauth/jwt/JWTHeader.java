package  com.project.oauth.jwt;
import org.json.simple.JSONObject;

public class JWTHeader {
	private JSONObject header = null;

	public JWTHeader() {
		header = new JSONObject();
	}
	@SuppressWarnings("unchecked")
	public void setHashAlgorithm(JWTHashAlgorithm jwtHashAlgorithm) 
	{
		switch (jwtHashAlgorithm) {
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
	String createHeader() {
		return header.toJSONString();
	}
	String getAlgorithm() {
		return (String) header.get("alg");
	}
	
}