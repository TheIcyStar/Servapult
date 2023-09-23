package autocrafter.ConsoleHandlers;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import autocrafter.DotenvLoader;
import autocrafter.Models.ConsoleHandler;
import io.github.cdimascio.dotenv.Dotenv;

public class MinecraftHandler extends ConsoleHandler {
    private HttpURLConnection webhookConnection = null;

    public static void main(String[] args) { //testing
        MinecraftHandler handler = new MinecraftHandler();
        handler.sendDiscordText("");
    }

    public MinecraftHandler(){
        // URL setup
        Dotenv dotenv = DotenvLoader.getDotenv();
        String urlString = dotenv.get("WEBHOOK_URL");
        if(urlString == null){
            System.out.println("⚠️ WEBHOOK_URL is not set! No messages will be sent to discord");
            return;
        }
        URL webhookUrl;
        try {
            webhookUrl = new URL(urlString);
        } catch (MalformedURLException e ) {
            System.out.println("⚠️ WEBHOOK_URL is malformed! No messages will be sent to discord");
            System.out.println(e);
            return;
        }

        // Set up the webhook connection
        try {
            this.webhookConnection = (HttpURLConnection)webhookUrl.openConnection();
            this.webhookConnection.setRequestMethod("POST");
            this.webhookConnection.setRequestProperty("Content-Type", "Application/json");
            this.webhookConnection.setRequestProperty("Accept", "Application/json");
            this.webhookConnection.setDoOutput(true);
        } catch (IOException e) {
            System.out.println("Error setting up webhook URL: ");
            System.out.println(e);
        }    
    }

    public void handleLine(String line){
        System.out.println("handled:"+line);

        //todo: fix output to file
        File file = new File("lastrun.log");
        try(FileWriter fr = new FileWriter(file, true)){
            fr.write(line);

        } catch(IOException e){
            System.out.println();
        }        
    }

    private void sendDiscordText(String line) {
        if(webhookConnection == null){
            return;
        }

        String payload = String.format("""
        {"username": "%s", "avatar_url": "https://i.imgur.com/XYz3c2w.png", "content": "%s"}            
        """, "OnlySixteenChars", "I'm the long lost cousin of OnlyTwentyCharacters");

        System.out.println(payload);
    
        try(DataOutputStream wr = new DataOutputStream(webhookConnection.getOutputStream())) {
            wr.writeBytes(payload);
        } catch (IOException e) {
            System.out.println("Error sending webhook: ");
            System.out.println(e);
        }

        // Get response and code
        int code = -1;
        StringBuffer response = new StringBuffer();
        try(BufferedReader in = new BufferedReader(new InputStreamReader(webhookConnection.getInputStream(), "utf-8"))) {
            code = webhookConnection.getResponseCode();
            
            String inputLine;
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        System.out.println("STATUS: "+code+" RESPONSE: \n"+response.toString());
    }
}
