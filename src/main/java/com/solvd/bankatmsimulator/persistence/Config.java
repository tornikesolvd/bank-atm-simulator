package com.solvd.bankatmsimulator.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);
    private static final Properties PROPS = new Properties();

    public static String URL;
    public static String USERNAME;
    public static String PASSWORD;
    public static Integer POOL_SIZE;

    static {
        try {
            load();
        } catch (ConfigurationException e) {
            log.error("Failed to load config.properties: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize database configuration", e);
        }
    }

    private Config() {
        throw new IllegalStateException("Utility class, do not instantiate!");
    }

    private static void load() throws ConfigurationException {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null)
                throw new ConfigurationException("config.properties file not found in resources");
            PROPS.load(input);
            URL = require("url");
            USERNAME = require("username");
            PASSWORD = require("password");
            try {
                POOL_SIZE = Integer.parseInt(require("poolSize"));
            } catch (NumberFormatException e) {
                throw new ConfigurationException("Invalid poolSize value in config.properties: must be a valid integer");
            }
        } catch (IOException e) {
            throw new ConfigurationException("Failed to read config.properties: " + e.getMessage());
        }
    }

    private static String require(String key) throws ConfigurationException {
        String value = PROPS.getProperty(key);
        if (value == null)
            throw new ConfigurationException("Missing required property: " + key);
        return value;
    }
}

