package io.github.danielzyla.pdcaclient.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyProvider {

    public static String getRestAppUrl() throws IOException {
        Properties prop = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("application.properties");
        prop.load(stream);
        return prop.getProperty("pdcaApp.rest.url");
    }
}
