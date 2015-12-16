package ca.csf.connect4.server;

import ca.csf.connect4.server.models.Game;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by Anthony on 14/12/2015.
 */
public class ServerLauncher {

    public static final String[] ERRORS = new String[] {
            "Given row count is not a valid number. Exiting.",
            "Given column count is not a valid number. Exiting.",
            "Given token count count to win the game is not a valid number. Exiting." };


    public ServerLauncher(String[] args) throws IOException {
        //displayLogo();
        if (args.length == 0) {
            exec();
        }
        for (int i = 0; i < args.length; ++i) {
            verifyArg(args[i], ERRORS[i]);
        }
        int rows = Integer.parseInt(args[0]);
        int columns = Integer.parseInt(args[1]);
        int tokens = Integer.parseInt(args[2]);
        exec(rows, columns, tokens);
    }

    private void displayLogo() {
        Path logoPath = Paths.get("/resources/logo");
        try (Stream<String> lines = Files.lines(logoPath, Charset.forName("UTF-8"))) {
            lines.forEach(line -> System.out.println(line));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exec(int rows, int columns, int tokens) throws IOException {
        GameConfig config = new GameConfig(rows, columns, tokens);
        new ServerController(config);
    }

    private void exec() throws IOException {
        exec(GameConfig.DEFAULT_COLUMN_COUNT, GameConfig.DEFAULT_ROW_COUNT, GameConfig.DEFAULT_NB_CELLS_TO_WIN);
    }

    private boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value, 10);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private void verifyArg(String argValue, String error) {
        if (!tryParseInt(argValue))
            System.out.println(error);
    }

    public static void main(String[] args) throws IOException {
        new ServerLauncher(args);
    }

}
