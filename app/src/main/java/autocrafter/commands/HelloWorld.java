package autocrafter.commands;

import net.dv8tion.jda.api.entities.Guild;
// import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class HelloWorld extends ListenerAdapter {
    private static final String COMMAND_NAME = "helloworld";
    private SlashCommandData commandData;
    
    public HelloWorld(Guild guild) {
        commandData = Commands.slash(COMMAND_NAME, "Says hello to the world");

        guild.getJDA().addEventListener(this);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals(COMMAND_NAME)) {
            event.reply("Hello, World!").queue();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equals("!helloWorld")) {
            event.getMessage().reply("Hello, World!");
        }
    }

    public SlashCommandData getCommandData() {
        return commandData;
    }

}
