package project.oauth.hashing;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public final class HmacSha256 {
	private HmacSha256()  { 
		 
	}
	private static String bytesToHex(byte[] bytes) {   
	        final char[] hexArray = "0123456789abcdef".toCharArray();
	        char[] hexChars = new char[bytes.length * 2];
	        for (int j = 0, v; j < bytes.length; j++) {
	            v = bytes[j] & 0xFF;
	            hexChars[j * 2] = hexArray[v >>> 4];
	            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	        }
	        return new String(hexChars);
	}
	public static String generateHashing(String message,byte[] key) throws InvalidKeyException, NoSuchAlgorithmException{
		Mac mac = Mac.getInstance("HmacSha256");
		mac.init(new SecretKeySpec(key, "HmacSha256"));
		byte[] macBytes = mac.doFinal(message.getBytes());
		return bytesToHex(macBytes);
	}

	
}
