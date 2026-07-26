package game.config.phase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import game.config.spawn.BossKind;
import game.config.spawn.BossSpawnEntry;
import game.config.spawn.EnemyKind;
import game.config.spawn.EnemySpawnEntry;
import game.config.spawn.EntitySpec;
import game.config.spawn.PowerUpKind;
import game.config.spawn.PowerUpSpawnEntry;

public class PhaseConfigParser {

    public Phase parse(String path) throws IOException {
        List<EntitySpec> specs = new ArrayList<>();
        int bossCount = 0;
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;

            while((line = reader.readLine()) != null){
                lineNumber++;
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                EntitySpec spec = parseLine(line,lineNumber);
                specs.add(spec);

                if(spec instanceof BossSpawnEntry){
                    bossCount++;
                }
            }
        }

        if (bossCount != 1){
            throw new IllegalStateException(
                "Arquivo de fase \"" + path + "\" deve conter exatamente uma linha CHEFE; encontradas " + bossCount + "."
            );
        }

        specs.sort(Comparator.comparingLong(EntitySpec::getWhenMs));

        return new Phase(specs);
    }

    private EntitySpec parseLine(String line, int lineNumber){
        String[] tokens = line.split("\\s"); // break the string in the empty spaces
        String keyword = tokens[0].toUpperCase();

        switch (keyword){
            case "INIMIGO":
                requireFieldCount(tokens, 5, lineNumber);
                return new EnemySpawnEntry(
                    parseEnemyKind(tokens[1], lineNumber),
                    parseLong(tokens[2], lineNumber, "quando"),
                    parseDouble(tokens[3], lineNumber, "posição x"),
                    parseDouble(tokens[4], lineNumber, "posição y")
                );
            case "CHEFE":
                requireFieldCount(tokens, 6, lineNumber);
                return new BossSpawnEntry(
                    parseBossKind(tokens[1], lineNumber),
                    parseInt(tokens[2], lineNumber, "pontos de vida"),
                    parseLong(tokens[3], lineNumber, "quando"),
                    parseDouble(tokens[4], lineNumber, "posição x"),
                    parseDouble(tokens[5], lineNumber, "posição y")
                );
            case "POWERUP":
                requireFieldCount(tokens, 5, lineNumber);
                return new PowerUpSpawnEntry(
                    parsePowerUpKind(tokens[1], lineNumber),
                    parseLong(tokens[2], lineNumber, "quando"),
                    parseDouble(tokens[3], lineNumber, "posição x"),
                    parseDouble(tokens[4], lineNumber, "posição y")
                );
            default:
                throw new IllegalArgumentException(
                    "Linha " + lineNumber + ": palavra chave desconhecida \"" + tokens[0] + "\""
                );
        }
    }

    private EnemyKind parseEnemyKind(String token, int lineNumber){
        switch (parseInt(token, lineNumber, "tipo")){
            case 1: return EnemyKind.TYPE_1;
            case 2: return EnemyKind.TYPE_2;
            default:
                throw new IllegalArgumentException(
                    "Linha " + lineNumber + ": tipo de inimigo inválido: \"" + token + "\""
                );
        }
    }

    private BossKind parseBossKind(String token, int lineNumber){
        switch (parseInt(token, lineNumber, "tipo")){
            case 1: return BossKind.TYPE_1;
            case 2: return BossKind.TYPE_2;
            default:
                throw new IllegalArgumentException(
                    "Linha " + lineNumber + ": tipo de chefe inválido: \"" + token + "\""
                );
        }
    }

    private PowerUpKind parsePowerUpKind(String token, int lineNumber){
        switch (parseInt(token, lineNumber, "tipo")){
            case 1: return PowerUpKind.SHIELD;
            case 2: return PowerUpKind.TRIPLE_SHOT;
            default:
                throw new IllegalArgumentException(
                    "Linha " + lineNumber + ": tipo de power-up inválido: \"" + token + "\""
                );
        }
    }

    private void requireFieldCount(String[] tokens, int expected, int lineNumber){
        if (tokens.length != expected){
            throw new IllegalStateException(
                "Linha " + lineNumber + ": esperava " + expected + " campos, encontrou " + tokens.length
            );
        }
    }

    private int parseInt(String token, int lineNumber, String field){
        try{
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Linha " + lineNumber + ": valor inválido para " + field + ": \"" + token + "\"", e
            );
        }
    }

    private long parseLong(String token, int lineNumber, String field){
        try {
            return Long.parseLong(token);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Linha " + lineNumber + ": valor inválido para " + field + ": \"" + token + "\"", e
            );
        }
    }

    private double parseDouble(String token, int lineNumber, String field){
        try {
            return Double.parseDouble(token);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Linha " + lineNumber + ": valor inválido para " + field + ": \"" + token + "\"", e
            );
        }
    }

}
