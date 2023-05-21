package autocrafter.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class StartServer extends ListenerAdapter {

    public static final String COMMAND_NAME = "start-server";

    public StartServer(Guild guild) {
        OptionData serverList = new OptionData(OptionType.STRING, "server", "Which server to set up");

        serverList.addChoice("latest", "minecraft-1.19.4");
        serverList.addChoice("astral", "create-astral");

        guild.updateCommands().addCommands(
            Commands.slash(COMMAND_NAME, "Starts a specified server")
                .addOptions(serverList)
        );

        guild.getJDA().addEventListener(this);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals(COMMAND_NAME)) {
            event.reply("Starting server!");
        }
    }
}
