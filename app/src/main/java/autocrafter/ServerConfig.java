package autocrafter;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NameNotFoundException;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlNode;

public class ServerConfig {
    private List<String> cmd;
    private String type;
    private boolean autoShutdown;
    private int graceMinutes;

    // public static void main(String args[]){
    //     ServerConfig serverConfig;

    //     try {
    //         serverConfig = new ServerConfig("latest");
    //     } catch (NameNotFoundException e) {
    //         System.out.println(e.toString());
    //         return;
    //     }

    //     System.out.println("cmd: "+serverConfig.cmd);
    //     System.out.println("type: "+serverConfig.type);
    //     System.out.println("autoShutdown: "+serverConfig.autoShutdown);
    //     System.out.println("graceMinutes: "+serverConfig.graceMinutes);
    // }

    public ServerConfig(String slug) throws NameNotFoundException{
        Dotenv dotenv = DotenvLoader.getDotenv();
        String configPath = dotenv.get("CONFIG_PATH");
        YamlMapping fullConfigData;

        //Load YAML data
        try {
            fullConfigData = Yaml.createYamlInput(
                new File(configPath)
            ).readYamlMapping();
        } catch (IOException e) {
            throw new NameNotFoundException("❌ Failed to find config file at: "+configPath);
        }

        //See if a config is available for that slug
        YamlMapping slugConfig;
        try {
            slugConfig = fullConfigData.yamlMapping("servers").yamlMapping(slug);
        } catch (NullPointerException e) {
            throw new NameNotFoundException("❌ Failed to find config data for server slug: "+slug);
        }


        //Set properties
        this.cmd = new ArrayList<String>();
        for(YamlNode item : slugConfig.yamlSequence("cmd")){
            cmd.add(item.toString()); //fixme
        }
        this.type = slugConfig.string("type");
        
        //Get optional restart polocies
        try {
            if (!slugConfig.yamlMapping("shutdowns").isEmpty()){
                this.autoShutdown = true;
                this.graceMinutes = slugConfig.yamlMapping("shutdowns").integer("grace-minutes");
            }
        } catch (NullPointerException e) {
            this.autoShutdown = false;
            this.graceMinutes = -1;
        }
    }

    public List<String> getCommand() {
        return cmd;
    }

    public String getType() {
        return type;
    }

    public boolean isAutoShutdown() {
        return autoShutdown;
    }

    public int getGraceMinutes() {
        return graceMinutes;
    }
}
