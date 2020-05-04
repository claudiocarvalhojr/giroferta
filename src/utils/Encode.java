package utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public final class Encode {
	
    public String codificaBase64Encoder(String valor) {
        return new Base64().encodeToString(valor.getBytes());
    }

    public String decodificaBase64Decoder(String valorCriptografado) {
        return new String(new Base64().decode(valorCriptografado));
    }

	public String encryptsPassword(String original) {
		String result = null;
		try {
			MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
			byte messageDigest[] = algorithm.digest(original.getBytes("UTF-8"));
			StringBuilder hexString = new StringBuilder();
			for (byte b : messageDigest)
			  hexString.append(String.format("%02X", 0xFF & b));
			result = hexString.toString();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
