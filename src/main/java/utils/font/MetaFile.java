package utils.font;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static utils.Constants.*;

public class MetaFile {

    private static final int PAD_TOP = 0;
    private static final int PAD_LEFT = 1;
    private static final int PAD_BOTTOM = 2;
    private static final int PAD_RIGHT = 3;

    private static final int DESIRED_PADDING = 3;

    private static final String SPLITTER = " ";
    private static final String NUMBER_SEPARATOR = ",";

    private double verticalPerPixelSize;
    private double horizontalPerPixelSize;
    private double spaceWidth;
    private int[] padding;
    private int paddingWidth;
    private int paddingHeight;

    private final Map<Integer, Character> metaData = new HashMap<>();

    private BufferedReader reader;
    private final Map<String, String> values = new HashMap<>();

    protected MetaFile(File file) {
        double aspectRatio = (double) WIDTH / (double) HEIGHT;
        try {
            reader = new BufferedReader(new FileReader(file));
            processNextLine();
            this.padding = getValuesOfVariable("padding");
            this.paddingWidth = padding[PAD_LEFT] + padding[PAD_RIGHT];
            this.paddingHeight = padding[PAD_TOP] + padding[PAD_BOTTOM];
            processNextLine();
            int lineHeightPixels = getValueOfVariable("lineHeight") - paddingHeight;
            verticalPerPixelSize = LINE_HEIGHT / (double) lineHeightPixels;
            horizontalPerPixelSize = verticalPerPixelSize / aspectRatio;
            int imageWidth = getValueOfVariable("scaleW");
            loadCharacterData(imageWidth);
            reader.close();
        } catch (IOException e) {
            System.err.println("Could not read font meta file: " + file.getName());
        }
    }

    private boolean processNextLine() {
        values.clear();
        String line = null;

        try {
            line = reader.readLine();
        } catch (IOException ignored) { }

        if (line == null)
            return false;

        for (String part : line.split(SPLITTER)) {
            String[] valuePairs = part.split("=");
            if (valuePairs.length == 2)
                values.put(valuePairs[0], valuePairs[1]);
        }

        return true;
    }

    private int[] getValuesOfVariable(String variable) {
        String[] numbers = values.get(variable).split(NUMBER_SEPARATOR);
        int[] actualValues = new int[numbers.length];
        for (int i = 0; i < actualValues.length; i++) {
            actualValues[i] = Integer.parseInt(numbers[i]);
        }
        return actualValues;
    }

    private int getValueOfVariable(String variable) {
        return Integer.parseInt(values.get(variable));
    }

    private void loadCharacterData(int imageWidth) {
        processNextLine();
        processNextLine();
        while (processNextLine()) {
            Character c = loadCharacter(imageWidth);
            if (c != null) {
                metaData.put(c.getId(), c);
            }
        }
    }

    private Character loadCharacter(int imageSize) {
        int id = getValueOfVariable("id");
        if (id == SPACE_ASCII) {
            this.spaceWidth = (getValueOfVariable("xadvance") - paddingWidth) * horizontalPerPixelSize;
            return null;
        }
        double xTex = ((double) getValueOfVariable("x") + (padding[PAD_LEFT] - DESIRED_PADDING)) / imageSize;
        double yTex = ((double) getValueOfVariable("y") + (padding[PAD_TOP] - DESIRED_PADDING)) / imageSize;
        int width = getValueOfVariable("width") - (paddingWidth - (2 * DESIRED_PADDING));
        int height = getValueOfVariable("height") - ((paddingHeight) - (2 * DESIRED_PADDING));
        double quadWidth = width * horizontalPerPixelSize;
        double quadHeight = height * verticalPerPixelSize;
        double xTexSize = (double) width / imageSize;
        double yTexSize = (double) height / imageSize;
        double xOff = (getValueOfVariable("xoffset") + padding[PAD_LEFT] - DESIRED_PADDING) * horizontalPerPixelSize;
        double yOff = (getValueOfVariable("yoffset") + (padding[PAD_TOP] - DESIRED_PADDING)) * verticalPerPixelSize;
        double xAdvance = (getValueOfVariable("xadvance") - paddingWidth) * horizontalPerPixelSize;
        return new Character(id, xTex, yTex, xTexSize, yTexSize, xOff, yOff, quadWidth, quadHeight, xAdvance);
    }

    protected double getSpaceWidth() {
        return spaceWidth;
    }

    protected Character getCharacter(int ascii) {
        return metaData.get(ascii);
    }

}
