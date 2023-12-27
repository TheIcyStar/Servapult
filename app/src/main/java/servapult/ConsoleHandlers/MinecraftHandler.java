package servapult.ConsoleHandlers;
// import java.io.File;
// import java.io.FileWriter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import io.github.cdimascio.dotenv.Dotenv;

import servapult.DotenvLoader;
import servapult.Models.ConsoleHandler;


public class MinecraftHandler extends ConsoleHandler {
    private HttpURLConnection webhookConnection = null;
    enum GameEventType {
        SERVER_READY,
        SERVER_CLOSE,

        PLAYER_JOIN,
        PLAYER_LEAVE,
        PLAYER_CHAT,
        PLAYER_ADVANCEMENT, //maybe not necessary?
        PLAYER_DEATH, //Minecraft death messages are quite complex, implement this someday?

        UNKNOWN
    }

    public class GameEvent {
        public GameEventType type;
        public String username;
        public String text;

        public GameEvent(GameEventType type, String username, String text){
            this.type = type;
            this.username = username;
            this.text = text;
        }

        public String getProfilePictureURL(){
            return "https://i.imgur.com/XYz3c2w.png";
        }
    }

    public static void main(String[] args) { //testing
        MinecraftHandler handler = new MinecraftHandler();
        handler.handleLine("[23:40:23] [Server thread/INFO]: Done (13.37s)! For help, type \"help\"");
        handler.handleLine("[23:41:00] [Server thread/INFO]: TheIcyStar joined the game");
        handler.handleLine("[23:41:22] [Server thread/INFO]: TheIcyStar has made the advancement [Stone Age]");
        handler.handleLine("[23:41:48] [Server thread/INFO]: TheIcyStar fell from a high place");
        handler.handleLine("[23:41:55] [Server thread/INFO]: <TheIcyStar> poggersky");
        handler.handleLine("[23:41:57] [Server thread/INFO]: TheIcyStar left the game");
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
        GameEvent newEvent = parseLineToGameEvent(line);
        sendDiscordText(newEvent);

        // File file = new File("lastrun.log");
        // try(FileWriter fr = new FileWriter(file, true)){
        //     fr.write(line+"\n");

        // } catch(IOException e){
        //     System.out.println();
        // }        
    }

    public GameEvent parseLineToGameEvent(String line){ //left off: finished parsing, needds to be tested with various lines. Main method in this class should work
        Pattern searchPattern;
        Matcher searchMatches;

        //Server Ready
        searchPattern = Pattern.compile("\\[Server thread/INFO]: Done \\(.*\\)! For help, type \"help\"");
        searchMatches = searchPattern.matcher(line);
        if (searchMatches.find()){
            return new GameEvent(GameEventType.SERVER_READY, null, null);
        }

        //TODO: Server Close
        searchPattern = Pattern.compile("I forgot to log the server close");
        searchMatches = searchPattern.matcher(line);
        if (searchPattern.matcher(line).find()){
            return new GameEvent(GameEventType.SERVER_CLOSE, null, null);
        }

        //Player join
        searchPattern = Pattern.compile("\\[Server thread/INFO]: ([\\w\\d]*) joined the game");
        searchMatches = searchPattern.matcher(line);
        if (searchPattern.matcher(line).find()){
            String username = searchMatches.group();
            return new GameEvent(GameEventType.PLAYER_JOIN, username, username + " joined the game");
        }

        //Player leave
        searchPattern = Pattern.compile("\\[Server thread/INFO]: ([\\w\\d]*) left the game");
        searchMatches = searchPattern.matcher(line);
        if (searchPattern.matcher(line).find()){
            String username = searchMatches.group();
            return new GameEvent(GameEventType.PLAYER_LEAVE, username, username + " left the game");
        }

        //Player chat
        searchPattern = Pattern.compile("\\[Server thread/INFO]: (<[\\w\\d]*>) (.*)");
        searchMatches = searchPattern.matcher(line);
        if (searchPattern.matcher(line).find()){
            String username = searchMatches.group(1);
            String chat = searchMatches.group(2);
            return new GameEvent(GameEventType.PLAYER_CHAT, username, chat);
        }


        
        return new GameEvent(GameEventType.PLAYER_CHAT, "OnlySixteenChars", "I'm the long lost cousin of OnlyTwentyCharacters");
    }

    private void sendDiscordText(GameEvent event) {
        if(webhookConnection == null){
            return;
        }

        //Grab username

        String payload = String.format("{\"username\": \"%s\", \"avatar_url\": \"%s\", \"content\": \"%s\"}\"",
         event.username, event.getProfilePictureURL(), event.text);

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
        //TODO: handle 4xx responses, (ratelmiits, invalid webhook urls, etc)
    }
}
