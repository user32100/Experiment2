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

public class Crypt {
	
	public static String Encrypt(String plainText, SecretKey key, String algorithm, String mode, IvParameterSpec iv ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		
		Cipher cipher = Cipher.getInstance(algorithm + "/" + mode + "/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] plainTextBytes  = plainText.getBytes("UTF-8");
		byte[] cipherTextBytes = cipher.doFinal(plainTextBytes);
		
		Base64.Encoder encoder = Base64.getEncoder();  
		String cipherText = encoder.encodeToString(cipherTextBytes);  
		
		return cipherText;
	}
	
	public static String Decrypt(String cipherTextBase64, SecretKey key, String algorithm, String mode, IvParameterSpec iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		
		Base64.Decoder decoder = Base64.getDecoder();    
		
		// Decoding Base64 string  
        String cipherText = new String(decoder.decode(cipherTextBase64));  
		
		Cipher cipher = Cipher.getInstance(algorithm + "/" +  mode + "/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		
		byte[] cipherTextBytes  = cipherText.getBytes("UTF-8");
		byte[] plainTextBytes = cipher.doFinal(cipherTextBytes);
		String plainText = new String(plainTextBytes);
		
		
		return plainText;
	}

}
