package utils.font;

import java.util.ArrayList;
import java.util.List;

public class Line {

    private final double maxLen;
    private final double spaceSize;

    private final List<Word> words = new ArrayList<>();
    private double currentLineLen = 0;

    protected Line(double spaceWidth, double fontSize, double maxLen) {
        this.maxLen = maxLen;
        this.spaceSize = spaceWidth * fontSize;
    }

    protected boolean addWord(Word word) {
        double plusLen = word.getWordWidth();
        plusLen += !words.isEmpty() ? spaceSize : 0;

        if (currentLineLen + plusLen > maxLen)
            return false;

        words.add(word);
        currentLineLen += plusLen;
        return true;
    }

    protected double getMaxLength() {
        return maxLen;
    }

    protected double getLineLength() {
        return currentLineLen;
    }

    protected List<Word> getWords() {
        return words;
    }

}
