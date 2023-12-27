package servapult.ConsoleHandlers;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import servapult.Models.ConsoleHandler;

public class MinecraftHandler extends ConsoleHandler {
    public void handleLine(String line){
        System.out.println("handled:"+line);

        //todo: fix output to file
        File file = new File("lastrun.log");
        try(FileWriter fr = new FileWriter(file, true)){
            fr.write("data");

        } catch(IOException e){
            System.out.println();
        }        
    }
}
