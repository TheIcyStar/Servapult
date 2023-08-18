/*
 * Handles external server starting
 */

package autocrafter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import javax.naming.NameNotFoundException;

import autocrafter.ConsoleHandlers.MinecraftHandler;

public class HosterClient extends Thread {
    public ProcessBuilder processBuilder;
    private static Process process;

    //testing
    public static void main(String[] args){
        HosterClient client = new HosterClient("latest");
        client.start();
    }

    public HosterClient(String serverSlug){
        ServerConfig serverConfig;

        try {
            serverConfig = new ServerConfig(serverSlug);
        } catch (NameNotFoundException e) {
            System.out.println("❌ Error getting server config");
            System.out.println(e);
            //Todo: Handle failed server starts better
            System.exit(1);
            return; //to shut up the linter
        }

        processBuilder = new ProcessBuilder(serverConfig.getCommand());
    }

    public void run(){
        try{
            process = processBuilder.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

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

            String errLine = stdErr.readLine();
            while(errLine != null){
                System.out.println(errLine);
                line = stdErr.readLine();
            }

            System.out.println("Done");

        } catch (Exception e){
            System.out.println("Error in process");
            System.out.println(e);
        }
    }

    public void halt() {
        process.destroy();
        processBuilder = null;
    }

    public static Process getServerProcess() {
        return process;
    }
}
