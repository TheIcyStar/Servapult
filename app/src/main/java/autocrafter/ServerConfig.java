package autocrafter;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;
import java.io.IOException;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;

public class ServerConfig {
    private String command;
    private String type;
    private boolean autoShutdown;
    private int graceMinutes;


    public ServerConfig(String slug){
        Dotenv dotenv = DotenvLoader.getDotenv();
        String configPath = dotenv.get("CONFIG_PATh");
        YamlMapping configData;

        try {
            configData = Yaml.createYamlInput(
                new File("asdf.yaml")
            ).readYamlMapping();
        } catch (IOException e) {
            System.out.println("❌ Failed to find config file at: "+configPath);
        }
        
        
    }
}
