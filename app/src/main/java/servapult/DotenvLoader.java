package servapult;

import io.github.cdimascio.dotenv.Dotenv;

public class DotenvLoader {
    
    private static Dotenv dotenv = Dotenv.load();

    public static Dotenv getDotenv() {
        return dotenv;
    }
}
