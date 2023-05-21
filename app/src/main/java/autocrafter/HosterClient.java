/*
 * Handles external server starting
 */

package autocrafter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;

import autocrafter.ConsoleHandlers.MinecraftHandler;

public class HosterClient {
    public ProcessBuilder processBuilder;
    private Process process;
    private String serverPath;
    private String[] serverArgs;

    //testing
    public static void main(String[] args){
        String[] javaArgs = {"-Xmx1024M", "-Xms1024M", "-jar"};
        HosterClient client = new HosterClient("Q:\\Servers\\DummyMC\\server.jar", javaArgs);
        client.start();
    }

    public HosterClient(String path, String[] args){
        ArrayList<String> list = new ArrayList<>();
        list.add("java");
        for(String arg : args){
            list.add(arg);
        }
        list.add(path);
        list.add("nogui");

        processBuilder = new ProcessBuilder(list);
    }

    public void start(){
        try{
            process = processBuilder.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            MinecraftHandler handler = new MinecraftHandler();

            String line;
            while (process.isAlive()) {
                line = stdInput.readLine();
                while(line != null){
                    handler.handleLine(line);
                    line = stdInput.readLine();
                }
                System.out.println(line);

                TimeUnit.MILLISECONDS.sleep(200);
            }

            System.out.println("Done");

        } catch (Exception e){
            System.out.println("Error in process");
            System.out.println(e);
        }
    }
}
