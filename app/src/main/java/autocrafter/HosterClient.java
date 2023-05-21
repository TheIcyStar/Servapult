/*
 * Handles external server starting
 */

package autocrafter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class HosterClient {
    public ProcessBuilder processBuilder;
    private Process process;
    private String serverPath;
    private String[] serverArgs;

    //testing
    public static void main(String[] args){
        HosterClient client = new HosterClient("", args);
        client.start();
    }

    public HosterClient(String path, String[] args){
        processBuilder = new ProcessBuilder("node", "Q:\\Proj\\SimpleOut\\app.js");
    }

    public void start(){
        try{
            process = processBuilder.start();

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while (process.isAlive()) {
                line = stdInput.readLine();
                System.out.println(line);
                TimeUnit.SECONDS.sleep(1);
            }

            System.out.println("Done");

        } catch (Exception e){
            System.out.println("Error in process");
            System.out.println(e);
        }
    }
}
