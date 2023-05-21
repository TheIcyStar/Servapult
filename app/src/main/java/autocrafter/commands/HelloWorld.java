package autocrafter.commands;

// import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelloWorld extends ListenerAdapter {
    private static final String COMMAND_NAME = "helloworld";
    
    

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals(COMMAND_NAME)) {
            event.reply("Hello, World!");
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().equals("!helloWorld")) {
            event.getMessage().reply("Hello, World!");
        }
    }

}
