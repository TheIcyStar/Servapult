package autocrafter.ConsoleHandlers;
import autocrafter.Models.ConsoleHandler;

public class MinecraftHandler extends ConsoleHandler {
    public void handleLine(String line){
        System.out.println("handled:"+line);
    }
}
