package model;

import texture.Texture;

public class TexturedModel {

    private final RawModel rawModel;
    private final Texture texture;

    public TexturedModel(RawModel rawModel, Texture texture) {
        this.rawModel = rawModel;
        this.texture = texture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public Texture getTexture() {
        return texture;
    }

}
