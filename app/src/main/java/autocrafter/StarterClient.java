package autocrafter;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class StarterClient {
    
    public void start() {

        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_BOT_TOKEN");


        JDA jda = JDABuilder.createDefault(token).build();

        
    }

}
