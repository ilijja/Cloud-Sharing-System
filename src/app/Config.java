package app;

import file.Files;
import servent.Network;
import servent.Servent;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class Config {

    public static final Random RANDOM = new Random(1);
    public static String WORKSPACE_PATH;
    public static int K;
    public static long FAILURE_SOFT;
    public static long FAILURE_HARD;
    public static Servent BOOTSTRAP;
    public static Servent LOCAL;
    public static Network NETWORK;
    public static Files WORKSPACE;
    public static Files STORAGE;

    public static Properties load(String path) {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(path));
        } catch (IOException e) {
            App.error("Cannot open properties file");
            System.exit(0);
        }

        WORKSPACE_PATH = properties.getProperty("workspace_path");
        K = Integer.parseInt(properties.getProperty("k"));
        FAILURE_SOFT = Long.parseLong(properties.getProperty("failure_soft"));
        FAILURE_HARD = Long.parseLong(properties.getProperty("failure_hard"));

        String bootstrapHost = properties.getProperty("bootstrap.host");
        int bootstrapPort = Integer.parseInt(properties.getProperty("bootstrap.port"));

        BOOTSTRAP = new Servent(bootstrapHost, bootstrapPort, 0);

        return properties;
    }

    public static void load(String path, int servent) {

        Properties properties = load(path);

        if (servent == 0) {
            LOCAL = BOOTSTRAP;
        } else {
            String serventHost = properties.getProperty("servent" + servent + ".host");
            int serventPort = Integer.parseInt(properties.getProperty("servent" + servent + ".port"));

            LOCAL = new Servent(serventHost, serventPort, servent);
        }

        NETWORK = new Network();
        WORKSPACE = new Files();
        STORAGE = new Files();


    }

}
