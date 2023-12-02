package com.pakotzy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Solution {
    private final int redRule;
    private final int greenRule;
    private final int blueRule;

    public Solution(int red, int green, int blue) {
        this.redRule = red;
        this.greenRule = green;
        this.blueRule = blue;
    }

    public int sumValidGames(List<Game> games) {
        int gameSum = 0;
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            boolean valid = true;
            for (GameSet gameSet : game.sets()) {
                if (gameSet.red() > redRule || gameSet.green() > greenRule || gameSet.blue() > blueRule) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                gameSum += i+1;
            }
        }

        return gameSum;
    }

    public long sumPowerLeastValidSets(List<Game> games) {
        long gameSum = 0L;
        for (int gi = 0, gj = games.size()-1; gi <= gj; gi++, gj--) {
            int[] fGame = getLeastSet(games.get(gi).sets());
            int[] lGame = new int[]{0, 0, 0};
            if (gi != gj) {
                lGame = getLeastSet(games.get(gj).sets());
            }

            gameSum += ((long) fGame[0] * fGame[1] * fGame[2]) + ((long) lGame[0] * lGame[1] * lGame[2]);
        }

        return gameSum;
    }

    private int[] getLeastSet(List<GameSet> sets) {
        int[] leastGamma = new int[]{0, 0, 0};
        for (int si = 0, sj = sets.size()-1; si <= sj; si++, sj--) {
            GameSet fSet = sets.get(si);
            GameSet lSet = sets.get(sj);

            leastGamma[0] = Math.max(leastGamma[0], Math.max(fSet.red(), lSet.red()));
            leastGamma[1] = Math.max(leastGamma[1], Math.max(fSet.green(), lSet.green()));
            leastGamma[2] = Math.max(leastGamma[2], Math.max(fSet.blue(), lSet.blue()));
        }

        return leastGamma;
    }

    public List<Game> compileGames(List<String> input) {
        List<Game> games = new ArrayList<>();
        for (String line : input) {
            parseGame(line).ifPresent(games::add);
        }

        return games;
    }

    private Optional<Game> parseGame(String line) {
        ParseState parseState = new ParseState();
        for (int j = line.length() - 1; j >= 0; j--) {
            char symbol = line.charAt(j);
            parseState.currentSymbol = symbol;

            if (Character.isAlphabetic(symbol) && parseState.colorCode == 0) {
                determineColor(parseState);
            } else if (Character.isDigit(symbol)) {
                addAmount(parseState);
            } else if (symbol == ',' || symbol == ';' || symbol == ':') {
                fillGamma(parseState);
                if (symbol == ';' || symbol == ':') {
                    fillSets(parseState);
                    if (symbol == ':') {
                        return Optional.of(new Game(parseState.gameSets));
                    }
                }
            }
        }

        return Optional.empty();
    }

    private void determineColor(ParseState parseState) {
        if (parseState.currentSymbol == 'd' || parseState.currentSymbol == 'e' || parseState.currentSymbol == 'n') {
            parseState.colorCode = parseState.currentSymbol;
        }
    }

    private void addAmount(ParseState parseState) {
        parseState.reverseAmountBuilder.append(Character.getNumericValue(parseState.currentSymbol));
    }

    private void fillGamma(ParseState parseState) {
        int colorAmount = Integer.parseInt(parseState.reverseAmountBuilder.reverse().toString());
        switch (parseState.colorCode) {
            case 'd' -> parseState.gamma[0] = colorAmount;
            case 'n' -> parseState.gamma[1] = colorAmount;
            case 'e' -> parseState.gamma[2] = colorAmount;
            default -> { /*skip*/ }
        }

        parseState.colorCode = 0;
        parseState.reverseAmountBuilder = new StringBuilder();
    }

    private void fillSets(ParseState parseState) {
        parseState.gameSets.add(new GameSet(parseState.gamma[0], parseState.gamma[1], parseState.gamma[2]));
        parseState.gamma = new int[]{0, 0, 0};
    }

    @SuppressWarnings("java:S1104")
    private static class ParseState {
        public char currentSymbol = 0;
        public char colorCode = 0;
        public StringBuilder reverseAmountBuilder = new StringBuilder();
        public int[] gamma = new int[]{0, 0, 0};
        public List<GameSet> gameSets = new ArrayList<>();
    }
}
