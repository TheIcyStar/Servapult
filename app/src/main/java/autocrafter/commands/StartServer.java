package autocrafter.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class StartServer extends ListenerAdapter {

    public static final String COMMAND_NAME = "startServer";

    public StartServer(JDA jda) {
        OptionData serverList = new OptionData(OptionType.STRING, "server", "Which server to set up");

        serverList.addChoice("latest", "minecraft-1.19.4");
        serverList.addChoice("astral", "create-astral");

        jda.updateCommands().addCommands(
            Commands.slash(COMMAND_NAME, "Starts a specified server")
                .addOptions(serverList)
        );
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("startServer")) {
            event.reply("Starting server!");
        }
    }
}
