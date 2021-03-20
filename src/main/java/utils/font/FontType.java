package utils.font;

import model.TextGUI;

import java.io.File;

public class FontType {

    private final int textureAtlas;
    private final TextMeshCreator loader;

    public FontType(int textureAtlas, File fontFile) {
        this.textureAtlas = textureAtlas;
        this.loader = new TextMeshCreator(fontFile);
    }

    public int getTextureAtlas() {
        return textureAtlas;
    }

    public TextMeshData loadText(TextGUI text) {
        return loader.createTextMesh(text);
    }

}
