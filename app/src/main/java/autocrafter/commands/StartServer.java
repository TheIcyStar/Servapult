package autocrafter.commands;

import java.io.IOException;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class StartServer extends ListenerAdapter {

    public static final String COMMAND_NAME = "start-server";
    private SlashCommandData commandData;

    public StartServer(Guild guild) {
        OptionData serverList = new OptionData(OptionType.STRING, "server", "Which server to set up");

        serverList.addChoice("latest", "minecraft-1.19.4");
        serverList.addChoice("astral", "create-astral");

        commandData = Commands.slash(COMMAND_NAME, "Starts a specified server")
            .addOptions(serverList);

        guild.getJDA().addEventListener(this);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals(COMMAND_NAME)) {
            String serverName = event.getOption("server").getAsString();

            event.reply("Starting server: " + serverName).queue();
            
            ProcessBuilder serverProcessBuilder = new ProcessBuilder("java", "-jar", "./servers/" + serverName + ".jar");

            try {
                Process serverProcess = serverProcessBuilder.start();
                serverProcess.destroy();
            } catch (IOException e) {
                event.reply("Failed to start! Error: " + e.getMessage());
            }
        }
    }

    public SlashCommandData getCommandData() {
        return commandData;
    }
}
