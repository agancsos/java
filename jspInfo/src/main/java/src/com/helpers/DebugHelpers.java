package com.helpers;

public class DebugHelpers {
	public static String getJarFile(Class c) {
		return c.getClassLoader().getResource(String.format("%s.class", c.getName().replace(".", "/"))).toString();
	}
}

