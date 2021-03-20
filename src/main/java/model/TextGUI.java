package model;

import utils.font.FontType;
import utils.font.Text;
import utils.math.Vector2f;
import utils.math.Vector3f;

public class TextGUI {

    private final String textString;
    private final float fontSize;

    private int textMeshVao;
    private int vertexCount;
    private final Vector3f color = new Vector3f(0f, 0f, 0f);

    private final Vector2f position;
    private final float lineMaxSize;
    private int numberOfLines;

    private final FontType font;

    private boolean centerText = false;

    public TextGUI(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
                   boolean centered) {
        this.textString = text;
        this.fontSize = fontSize;
        this.font = font;
        this.position = position;
        this.lineMaxSize = maxLineLength;
        this.centerText = centered;
        Text.loadText(this);
    }

    public void remove() {
        Text.removeText(this);
    }

    public FontType getFont() {
        return font;
    }

    public void setColor(float r, float g, float b) {
        color.x = r;
        color.y = g;
        color.z = b;
    }

    public Vector3f getColor() {
        return color;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public Vector2f getPosition() {
        return position;
    }

    public int getMesh() {
        return textMeshVao;
    }

    public void setMeshInfo(int vao, int verticesCount) {
        this.textMeshVao = vao;
        this.vertexCount = verticesCount;
    }

    public int getVertexCount() {
        return this.vertexCount;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setNumberOfLines(int number) {
        this.numberOfLines = number;
    }

    public boolean isCentered() {
        return centerText;
    }

    public float getMaxLineSize() {
        return lineMaxSize;
    }

    public String getTextString() {
        return textString;
    }
    
}
