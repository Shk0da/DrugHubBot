package org.telegram.drughubbot;

import org.telegram.telegrambots.logging.BotLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static volatile Config instance;

    private static String APP_NAME = "";
    private static String APP_API_KEY = "";
    private static String DB_LINK = "";
    private static String DB_DRIVER = "";
    private static String DB_USER = "";
    private static String DB_PASS = "";

    private static final String LOGTAG = "CONFIG";

    private Config() throws IOException {

        Properties props = new Properties();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream configIni = classloader.getResourceAsStream("config.ini");

        props.load(configIni);

        APP_NAME = props.getProperty("APP_NAME");
        APP_API_KEY = props.getProperty("APP_API_KEY");
        DB_LINK = props.getProperty("DB_LINK");
        DB_DRIVER = props.getProperty("DB_DRIVER");
        DB_USER = props.getProperty("DB_USER");
        DB_PASS = props.getProperty("DB_PASS");
    }

    public static Config getInstance() {
        Config localInstance = instance;
        if (localInstance == null) {
            synchronized (Config.class) {
                localInstance = instance;
                if (localInstance == null) {
                    try {
                        instance = localInstance = new Config();
                    } catch (IOException e) {
                        BotLogger.error(LOGTAG, e);
                    }
                }
            }
        }
        return localInstance;
    }

    public String getAppName() {
        return APP_NAME;
    }

    public String getAppApiKey() {
        return APP_API_KEY;
    }

    public String getDbLink() {
        return DB_LINK;
    }

    public String getDbDriver() {
        return DB_DRIVER;
    }

    public String getDbUser() {
        return DB_USER;
    }

    public String getDbPass() {
        return DB_PASS;
    }
}
