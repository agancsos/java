package com.common.propertystore;

public interface IPropertyStore {
	public void setPropertyValue(String key, Object value);
	public Object getPropertyValue(String key);
}
