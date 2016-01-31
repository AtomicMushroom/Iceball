package network.iceball_bc.util;

import network.iceball_bc.Iceball;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Created by Floris on 28-09-15.
 */
public class Config {

    Properties properties;
    OutputStream output = null;
    private final Map<String, String> configuration;

    private File getProperties() {
        return propertiesFile;
    }

    private File propertiesFile;
    private File directory;
    private Iceball plugin;

    public Config(Iceball plugin){
        this.plugin = plugin;
        properties = new Properties();

        configuration = new TreeMap<>();
        properties.put("server", "localhost");
        properties.put("port", "3306");
        properties.put("user", "minecraft");
        properties.put("password", "diamond123");
        properties.put("database", "iceball");
        properties.put("enabled", "false");
        load();
    }

    private void load() {
        try {
            directory = new File(plugin.getDataFolder() + "");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            propertiesFile = new File(plugin.getDataFolder() + "/database.properties");
            if (!propertiesFile.exists()) {
                propertiesFile.createNewFile();
                resetValues();
            }
            InputStream input = new FileInputStream(propertiesFile.getAbsolutePath());
            properties.load(input);
            boolean valid = true;
            for (String key : configuration.keySet()) {
                if (!properties.containsKey(key)) {
                    valid = false;
                }
            }
            if (!valid) {
                resetValues();
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }  finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void resetValues(){
        try {
            output = new FileOutputStream(propertiesFile); //ONLY USE THIS IF YOU WANT TO OVERWRITE THE FILE
        } catch (FileNotFoundException e) {
            load();
        }

        for (Map.Entry<String, String> entry : configuration.entrySet()){
            properties.setProperty(entry.getKey(), entry.getValue());
        }

        try {
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getDatabaseConfiguration(){
        Properties prop = new Properties();
        String user = (String) properties.get("user");
        String port = (String) properties.get("port");
        String password = (String) properties.get("password");
        String database = (String) properties.get("database");
        String server = (String) properties.get("server");

        prop.setProperty("enabled", (String) properties.get("enabled"));
        prop.setProperty("dataSource.user", user);
        prop.setProperty("dataSource.password", password);

        prop.setProperty("dataSource.serverName", server);
        prop.setProperty("dataSource.portNumber", port);

        prop.setProperty("dataSource.databaseName", database);
        prop.setProperty("dataSource.url", "jdbc:mysql://" + properties.get("server") + ":" + properties.get("port") + "/?user=" + user + "&password=" + password);

        prop.setProperty("dataSourceClassName", "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        prop.setProperty("dataSource.prepStmtCacheSqlLimit", "2048");
        prop.setProperty("dataSource.prepStmtCacheSize", "250");
        prop.setProperty("dataSource.cachePrepStmts", "true");
        return prop;
    }
}
