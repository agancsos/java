package com.cacheMoney.services;
import java.util.Base64;

public class SecurityService {
	public static String getBase64Decoded(String encoded) {
		return new String(Base64.getDecoder().decode(encoded));
	}
}

