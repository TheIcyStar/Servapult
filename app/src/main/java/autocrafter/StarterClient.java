package autocrafter;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;

import autocrafter.commands.*;

/**
 * Runs the Discord bot which allows for users to start and stop game servers
 */
public class StarterClient {
    
    /**
     * Starts the bot
     */
    public void start() {

        // Load relevant .env variables
        Dotenv dotenv = DotenvLoader.getDotenv();
        String token = dotenv.get("DISCORD_BOT_TOKEN");
        // Test token
        if (token == null) {
            System.err.println("Token in .env is null!");
            return;
        }
        String guildId = dotenv.get("DISCORD_SERVER_ID");
        // Test guild ID
        if (guildId == null) {
            System.err.println("Guild ID in .env is null!");
            return;
        }

        // Start up the bot
        JDA jda = JDABuilder.createDefault(token).build();

        try {
            jda.awaitReady();
        } catch(InterruptedException exception) {
            System.err.println(exception.getMessage());
        }

        System.out.println("Guild ID: " + guildId);
        Guild guild = jda.getGuildById(guildId);
        // Test guild ID again to see if it exists
        if (guild == null ) {
            System.err.println("Guild with ID " + guildId + " does not exist!");
            return;
        }

        // Load commands
        ArrayList<SlashCommandData> slashCommands = new ArrayList<SlashCommandData>();
        slashCommands.add(new HelloWorld(guild).getCommandData());
        slashCommands.add(new StartServer(guild).getCommandData());
        slashCommands.add(new StopServer(guild).getCommandData());

        guild.updateCommands().addCommands(slashCommands).queue();
        // Hello, World!
    }

}
