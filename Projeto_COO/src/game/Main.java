package game;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.config.GameConfig;
import game.config.GameConfigParser;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final String DEFAULT_CONFIG_PATH = "config/game.txt";

    public static void main(String[] args) {
        String configPath = args.length > 0 ? args[0] : DEFAULT_CONFIG_PATH;
        LOGGER.info(() -> "Reading config from \"" + configPath + "\" (resolved to " + new File(configPath).getAbsolutePath() + ")");

        GameConfig config;
        try {
            config = new GameConfigParser().parse(configPath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Não foi possível ler o arquivo de configuração \"" + configPath + "\". "
                + "Verifique se o caminho está correto em relação ao diretório de onde o programa foi executado.", e);
            System.exit(1);
            return;
        }

        new Game(config).run();
    }
}
