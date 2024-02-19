package com.jmagent.services;
import com.common.propertystore.PListPropertyStore;
import com.common.propertystore.IPropertyStore;

public class ConfigurationService {
    private IPropertyStore store;
    private static ConfigurationService instance;

    private ConfigurationService(String path) {
        this.store = new PListPropertyStore(path);
    }

    public static ConfigurationService getInstance(String path) {
        if (path != null) {
            instance = new ConfigurationService(path);
        }
        return instance;
    }

    public Object getProperty(String name) {
        return this.store.getPropertyValue(name);
    }
}