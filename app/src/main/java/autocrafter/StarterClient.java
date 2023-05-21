package autocrafter;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import autocrafter.commands.*;

public class StarterClient {
    
    public void start() {

        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_BOT_TOKEN");
        String guildId = dotenv.get("DISCORD_SERVER_ID");

        // Start up the bot
        JDA jda = JDABuilder.createDefault(token).build();

        System.out.println("Guild ID: " + guildId);
        Guild guild = jda.getGuildById(guildId);

        // Load commands
        jda.addEventListener(new HelloWorld());
        new StartServer(guild);

        guild.updateCommands().addCommands(
            Commands.slash("helloworld", "Test slash command!")
        );
        // Hello, World!
    }

}
