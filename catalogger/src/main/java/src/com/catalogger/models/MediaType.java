package com.catalogger.models;

public enum MediaType {
	BOOK,
	DVD,
	CD,
	TAPE;

    public static int getOrdinal(ObjectType ot) {
        for (int i = 0; i < ObjectType.values().length; i++) {
            if (ObjectType.values()[i] == ot) {
                return i;
            }
        }
        return -1;
    }
}
