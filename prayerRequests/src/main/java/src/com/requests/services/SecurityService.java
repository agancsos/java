package com.requests.services;
import java.util.Base64;

public class SecurityService {
	public static String getBase64Decoded(String encoded) {
		return new String(Base64.getDecoder().decode(encoded));
	}

	public static String getBase64Encoded(String raw) {
		return Base64.getEncoder().encodeToString(raw.getBytes());
	}
}

