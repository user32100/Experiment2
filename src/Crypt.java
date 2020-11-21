import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypt {
	
	public static String Encrypt(String plainText, String encodedKey, String algorithm, String mode, String iv ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		
		// decode the base64 encoded string
		byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
		// rebuild key using SecretKeySpec
		SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, algorithm); 
		
		// IvParameterSpec iv
		byte[] bytesIV = iv.getBytes("UTF-8");

	    /* KEY + IV setting */
	    IvParameterSpec IV = new IvParameterSpec(bytesIV);
		
		Cipher cipher = Cipher.getInstance(algorithm + "/" + mode + "/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, IV);
		byte[] plainTextBytes  = plainText.getBytes("UTF-8");
		byte[] cipherTextBytes = cipher.doFinal(plainTextBytes);
		
		Base64.Encoder encoder = Base64.getEncoder();  
		//String cipherText = encoder.encodeToString(cipherTextBytes);  
		String cipherText = new String(encoder.encode(cipherTextBytes));
		return cipherText;
	}
	
	public static String Decrypt(String cipherTextBase64, String encodedKey, String algorithm, String mode, String iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		
		Base64.Decoder decoder = Base64.getDecoder();    
		
		// Decoding Base64 string  
        //String cipherText = new String(decoder.decode(cipherTextBase64.getBytes()));  
		byte[] cipherText = decoder.decode(cipherTextBase64.getBytes());
        
        // decode the base64 encoded string
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
     	
        
        // rebuild key using SecretKeySpec
     	SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, algorithm); 
        
     	
     	// IvParameterSpec iv
     	byte[] bytesIV = iv.getBytes("UTF-8");

     	/* KEY + IV setting */
     	IvParameterSpec IV = new IvParameterSpec(bytesIV);
        
		Cipher cipher = Cipher.getInstance(algorithm + "/" +  mode + "/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, IV);
		
		//byte[] cipherTextBytes  = cipherText.getBytes("UTF-8");
		byte[] plainTextBytes = cipher.doFinal(cipherText);
		String plainText = new String(plainTextBytes);
		
		
		return plainText;
	}

}
