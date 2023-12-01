package com.pakotzy;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Solution {
    public long calculateCalibration(InputStream inputStream) throws IOException {
        State state = new State();

        try (ReadableByteChannel channel = Channels.newChannel(inputStream)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (channel.isOpen() && channel.read(buffer) > 0) {
                buffer.flip();
                findCalibrationValue(buffer, state);
                buffer.clear();
            }
        } catch (ClosedByInterruptException ignored) { /*user interrupted the input */ }

        return state.getSum();
    }

    private void findCalibrationValue(ByteBuffer buffer, State state) throws ClosedByInterruptException {
        while (buffer.hasRemaining()) {
            byte aByte = buffer.get();
            if (aByte == '\n') {
                if (state.isFirstEmpty()) {
                    throw new ClosedByInterruptException();
                }

                state.addSum();
                state.newLine();
            } else if (Character.isDigit(aByte)) {
                if (state.isFirstEmpty()) {
                    state.setFirst(aByte);
                }

                state.setLast(aByte);
            } else if (Character.isAlphabetic(aByte)) {
                if (state.isFirstEmpty()) {
                    state.addFirstLetterIfApplicable((char) aByte);
                }

                state.addLastLetterIfApplicable((char) aByte);
            }
        }
    }

    private static class State {
        private final List<Character> firstLetters = new ArrayList<>(5);
        private final List<DigitWord> firstPossibilities = new ArrayList<>(DigitWord.values().length);
        private final List<Character> lastLetters = new ArrayList<>(5);
        private final List<DigitWord> lastPossibilities = new ArrayList<>(DigitWord.values().length);

        private int first = -1;
        private int last = -1;
        private long sum = 0;

        public void newLine() {
            first = -1;
            firstLetters.clear();
            firstPossibilities.clear();

            last = -1;
            lastLetters.clear();
            lastPossibilities.clear();
        }

        public void addFirstLetterIfApplicable(char letter) {
            Optional<DigitWord> digitWord = addLetterIfApplicable(letter, firstLetters, firstPossibilities);
            digitWord.ifPresent(word -> first = word.ordinal() + 1);
        }

        public void addLastLetterIfApplicable(char letter) {
            Optional<DigitWord> digitWord = addLetterIfApplicable(letter, lastLetters, lastPossibilities);
            digitWord.ifPresent(word -> last = word.ordinal() + 1);
        }

        /**
         * @return present optional if fully completed
         */
        private Optional<DigitWord> addLetterIfApplicable(char letter, List<Character> letters, List<DigitWord> possibilities) {
            Optional<DigitWord> completeWord = Optional.empty();
            if (letters.isEmpty()) {
                initializePossibilities(letter, letters, possibilities);
            } else {
                completeWord = reviseOrCompletePossibilities(letter, letters, possibilities);
            }

            return completeWord;
        }

        private Optional<DigitWord> reviseOrCompletePossibilities(char letter, List<Character> letters, List<DigitWord> possibilities) {
            List<DigitWord> revisedPossibilities = new ArrayList<>(possibilities.size());
            for (DigitWord digitWord : possibilities) {
                if (digitWord.isCompletes(letters, letter)) {
                    letters.clear();
                    possibilities.clear();

                    addLetterIfApplicable(letter, letters, possibilities);
                    return Optional.of(digitWord);
                } else if (digitWord.isStillApplicable(letters, letter)) {
                    letters.add(letter);
                    revisedPossibilities.add(digitWord);
                }
            }

            if (revisedPossibilities.isEmpty()) {
                char prevLetter = letters.getLast();

                letters.clear();
                possibilities.clear();

                List<Character> lookBehindLetters = List.of(prevLetter, letter);
                List<DigitWord> all = DigitWord.findAll(lookBehindLetters);
                if (all.isEmpty()) {
                    addLetterIfApplicable(letter, letters, possibilities);
                } else {
                    letters.addAll(lookBehindLetters);
                    possibilities.addAll(all);
                }
            } else {
                possibilities.clear();
                possibilities.addAll(revisedPossibilities);
            }

            return Optional.empty();
        }

        private void initializePossibilities(char letter, List<Character> letters, List<DigitWord> possibilities) {
            List<DigitWord> initialPossibilities = DigitWord.findInitialPossibilities(letter);
            if (!initialPossibilities.isEmpty()) {
                letters.add(letter);
                possibilities.addAll(initialPossibilities);
            }
        }

        public boolean isFirstEmpty() {
            return first == -1;
        }

        public void setFirst(byte first) {
            this.first = Character.getNumericValue(first);
            this.firstLetters.clear();
            this.firstPossibilities.clear();
        }

        public void setLast(byte last) {
            this.last = Character.getNumericValue(last);
            this.lastLetters.clear();
            this.lastPossibilities.clear();
        }

        public long getSum() {
            if (!isFirstEmpty()) {
                addSum();
            }
            return sum;
        }

        public void addSum() {
            this.sum += Short.parseShort(Integer.toString(first) + last);
            this.first = -1;
            this.last = -1;
        }
    }

    private enum DigitWord {
        ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE;

        public boolean isCompletes(List<Character> chars, char newChar) {
            return this.name().toCharArray().length == chars.size() + 1 && Character.toUpperCase(newChar) == this.name().toCharArray()[this.name().toCharArray().length - 1];
        }

        public boolean isStillApplicable(List<Character> chars, char newChar) {
            return chars.size() <= this.name().toCharArray().length - 1 && Character.toUpperCase(newChar) == this.name().toCharArray()[chars.size()];
        }

        public static List<DigitWord> findInitialPossibilities(char firstLetter) {
            List<DigitWord> possibilities = new ArrayList<>(DigitWord.values().length);

            for (DigitWord digitWord : DigitWord.values()) {
                if (digitWord.name().charAt(0) == Character.toUpperCase(firstLetter)) {
                    possibilities.add(digitWord);
                }
            }

            return possibilities;
        }

        public static List<DigitWord> findAll(List<Character> chars) {
            List<DigitWord> possibilities = new ArrayList<>(DigitWord.values().length);
            String prefix = chars.stream().map(String::valueOf).map(String::toUpperCase).collect(Collectors.joining());

            for (DigitWord digitWord : DigitWord.values()) {
                if (digitWord.name().startsWith(prefix)) {
                    possibilities.add(digitWord);
                }
            }

            return possibilities;
        }
    }
}
