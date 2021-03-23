package model;

public class TextureParticle {

    private final int textureId;
    private final int rows;

    public TextureParticle(int textureId, int rows) {
        this.textureId = textureId;
        this.rows = rows;
    }

    public int getTextureId() {
        return textureId;
    }

    public int getRows() {
        return rows;
    }

}
