package utils.font;

import model.TextGUI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static utils.Constants.LINE_HEIGHT;
import static utils.Constants.SPACE_ASCII;

public class TextMeshCreator {

    private final MetaFile metaData;

    protected TextMeshCreator(String metaFile) {
        metaData = new MetaFile(metaFile);
    }

    protected TextMeshData createTextMesh(TextGUI text) {
        List<Line> lines = createStructure(text);
        return createQuadVertices(text, lines);
    }

    private List<Line> createStructure(TextGUI text) {
        char[] chars = text.getTextString().toCharArray();
        List<Line> lines = new ArrayList<>();
        Line currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
        Word currentWord = new Word(text.getFontSize());
        for (char c : chars) {
            if ((int) c == SPACE_ASCII) {
                boolean added = currentLine.addWord(currentWord);
                if (!added) {
                    lines.add(currentLine);
                    currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
                    currentLine.addWord(currentWord);
                }
                currentWord = new Word(text.getFontSize());
                continue;
            }
            Character character = metaData.getCharacter(c);
            currentWord.addCharacter(character);
        }
        completeStructure(lines, currentLine, currentWord, text);
        return lines;
    }

    private void completeStructure(List<Line> lines, Line currentLine, Word currentWord, TextGUI text) {
        boolean added = currentLine.addWord(currentWord);
        if (!added) {
            lines.add(currentLine);
            currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
            currentLine.addWord(currentWord);
        }
        lines.add(currentLine);
    }

    private TextMeshData createQuadVertices(TextGUI text, List<Line> lines) {
        double cursorX = 0f;
        double cursorY = 0f;
        List<Float> vertices = new ArrayList<>();
        List<Float> textureCoords = new ArrayList<>();
        for (Line line : lines) {
            if (text.isCentered()) {
                cursorX = (line.getMaxLength() - line.getLineLength()) / 2;
            }
            for (Word word : line.getWords()) {
                for (Character letter : word.getCharacters()) {
                    addVerticesForCharacter(cursorX, cursorY, letter, text.getFontSize(), vertices);
                    addTexCoords(textureCoords, letter.getXTextureCoord(), letter.getYTextureCoord(),
                            letter.getXMaxTextureCoord(), letter.getYMaxTextureCoord());
                    cursorX += letter.getXAdvance() * text.getFontSize();
                }
                cursorX += metaData.getSpaceWidth() * text.getFontSize();
            }
            cursorX = 0;
            cursorY += LINE_HEIGHT * text.getFontSize();
        }
        return new TextMeshData(listToArray(vertices), listToArray(textureCoords));
    }

    private void addVerticesForCharacter(double cursorX, double cursorY, Character character, double fontSize,
                                         List<Float> vertices) {
        double x = cursorX + (character.getXOffset() * fontSize);
        double y = cursorY + (character.getYOffset() * fontSize);
        double maxX = x + (character.getSizeX() * fontSize);
        double maxY = y + (character.getSizeY() * fontSize);
        double properX = (2 * x) - 1;
        double properY = (-2 * y) + 1;
        double properMaxX = (2 * maxX) - 1;
        double properMaxY = (-2 * maxY) + 1;
        addVertices(vertices, properX, properY, properMaxX, properMaxY);
    }

    private static void addVertices(List<Float> vertices, double x, double y, double maxX, double maxY) {
        vertices.add((float) x);
        vertices.add((float) y);
        vertices.add((float) x);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) y);
        vertices.add((float) x);
        vertices.add((float) y);
    }

    private static void addTexCoords(List<Float> texCoords, double x, double y, double maxX, double maxY) {
        texCoords.add((float) x);
        texCoords.add((float) y);
        texCoords.add((float) x);
        texCoords.add((float) maxY);
        texCoords.add((float) maxX);
        texCoords.add((float) maxY);
        texCoords.add((float) maxX);
        texCoords.add((float) maxY);
        texCoords.add((float) maxX);
        texCoords.add((float) y);
        texCoords.add((float) x);
        texCoords.add((float) y);
    }


    private static float[] listToArray(List<Float> listOfFloats) {
        float[] array = new float[listOfFloats.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = listOfFloats.get(i);
        }
        return array;
    }

}
