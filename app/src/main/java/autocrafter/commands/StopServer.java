package autocrafter.commands;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class StopServer extends ListenerAdapter {

    public static final String COMMAND_NAME = "stop-server";
    private SlashCommandData commandData;

    public StopServer(Guild guild) {
        commandData = Commands.slash(COMMAND_NAME, "Stop the currently running server");

        guild.getJDA().addEventListener(this);
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        if (event.getName().equals(COMMAND_NAME)) {
            autocrafter.commands.StartServer.getHosterClient().stop();
        }
    }
    
    public SlashCommandData getCommandData() {
        return commandData;
    }
}