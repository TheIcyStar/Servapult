package autocrafter.commands;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import autocrafter.HosterClient;

public class StartServer extends ListenerAdapter {

    public static final String COMMAND_NAME = "start-server";
    private SlashCommandData commandData;

    private static HosterClient client;

    public StartServer(Guild guild) {
        OptionData serverList = new OptionData(OptionType.STRING, "server", "Which server to set up");

        serverList.addChoice("latest", "minecraft-1.19.4");
        serverList.addChoice("astral", "create-astral");

        commandData = Commands.slash(COMMAND_NAME, "Starts a specified server")
            .addOptions(serverList);

        guild.getJDA().addEventListener(this);
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        if (event.getName().equals(COMMAND_NAME)) {
            OptionMapping serverOption = event.getOption("server");
            if (serverOption == null) {
                System.err.println("Option was null");
                return;
            }
            String serverName = serverOption.getAsString();

            event.reply("Starting server: " + serverName).queue();
            
            client = new HosterClient(serverName);
            client.start();
        }
    }

    public SlashCommandData getCommandData() {
        return commandData;
    }

    public static HosterClient getHosterClient() {
        return client;
    }
}
