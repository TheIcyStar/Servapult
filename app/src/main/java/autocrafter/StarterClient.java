package autocrafter;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;

import autocrafter.commands.*;

public class StarterClient {
    
    public void start() {

        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_BOT_TOKEN");
        String guildId = dotenv.get("DISCORD_SERVER_ID");

        // Start up the bot
        JDA jda = JDABuilder.createDefault(token).build();

        try {
            jda.awaitReady();
        } catch(InterruptedException exception) {
            System.err.println(exception.getMessage());
        }

        System.out.println("Guild ID: " + guildId);
        Guild guild = jda.getGuildById(guildId);

        // Load commands
        ArrayList<SlashCommandData> slashCommands = new ArrayList<SlashCommandData>();
        slashCommands.add(new HelloWorld(guild).getCommandData());
        slashCommands.add(new StartServer(guild).getCommandData());

        guild.updateCommands().addCommands(slashCommands).queue();
        // Hello, World!
    }

}
