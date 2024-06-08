package com.catalogger.models;

public enum TraceCategory {
	NONE,
	APPLICATION,
	REST,
	TIMER;


    public static int getOrdinal(ObjectType ot) {
        for (int i = 0; i < ObjectType.values().length; i++) {
            if (ObjectType.values()[i] == ot) {
                return i;
            }
        }
        return -1;
    }
}

