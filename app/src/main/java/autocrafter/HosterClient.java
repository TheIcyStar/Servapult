/*
 * Handles external server starting
 */

package autocrafter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import autocrafter.ConsoleHandlers.MinecraftHandler;

public class HosterClient extends Thread {
    public ProcessBuilder processBuilder;
    private static Process process;
    // private String serverPath;
    // private String[] serverArgs;

    //testing
    public static void main(String[] args){
        HosterClient client = new HosterClient("latest");
        client.start();
    }

    public HosterClient(String serverSlug){
        

        processBuilder = new ProcessBuilder("java", "-jar", "server.jar");
        processBuilder.directory(new File("Q:\\Servers\\DummyMC"));
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
