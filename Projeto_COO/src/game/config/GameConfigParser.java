package game.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * GameConfigParser
 */
public class GameConfigParser {
    public GameConfig parse(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))){
            int playerHealth = parseIntline(reader.readLine(), "pontos de vida do jogador");
            int phaseCount = parseIntline(reader.readLine(), "numero de fases");

            List<String> phaseFileNames = new ArrayList<>();

            for (int i = 1; i <= phaseCount; i++){
                String line = reader.readLine();

                if (line == null || line.trim().isEmpty()){
                    throw new IllegalStateException(
                        "Esperava o nome do arquivo da fase " + i + " de " + phaseCount + ", mas o arquivo terminou antes."
                    );
                }

                phaseFileNames.add(line.trim());
            }

            return new GameConfig(playerHealth, phaseFileNames);
        }
    }

    private int parseIntline(String line, String description){
        if (line == null)
            throw new IllegalStateException("Arquivo de configuração incompleto: faltando " + description + ".");

        try {
            return Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Valor inválido para " + description + ": \"" + line + "\"", e);
        }
    }
}
