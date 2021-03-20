package utils.font;


import java.util.ArrayList;
import java.util.List;

public class Word {

    private final List<Character> characters = new ArrayList<>();
    private double width = 0;
    private final double fontSize;

    protected Word(double fontSize) {
        this.fontSize = fontSize;
    }

    protected void addCharacter(Character character) {
        characters.add(character);
        width += character.getXAdvance() * fontSize;
    }

    protected List<Character> getCharacters() {
        return characters;
    }

    protected double getWordWidth() {
        return width;
    }

}
