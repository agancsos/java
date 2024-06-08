package com.catalogger.models;

public enum ObjectType {
	NONE,
	AUTHOR,
	PUBLISHER,
	TITLE,
	TITLE_COPY,
	CUSTOMER,
	BORROW;
	
	public static int getOrdinal(ObjectType ot) {
		for (int i = 0; i < ObjectType.values().length; i++) {
			if (ObjectType.values()[i] == ot) {
				return i;
			}
		}
		return -1;
	}
}
	
