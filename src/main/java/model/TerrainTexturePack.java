package model;

public class TerrainTexturePack {

    private final Texture backgroundTexture;
    private final Texture rTexture;
    private final Texture gTexture;
    private final Texture bTexture;

    public TerrainTexturePack(Texture backgroundTexture, Texture rTexture, Texture gTexture, Texture bTexture) {
        this.backgroundTexture = backgroundTexture;
        this.rTexture = rTexture;
        this.gTexture = gTexture;
        this.bTexture = bTexture;
    }

    public Texture getBackgroundTexture() {
        return backgroundTexture;
    }

    public Texture getRTexture() {
        return rTexture;
    }

    public Texture getGTexture() {
        return gTexture;
    }

    public Texture getBTexture() {
        return bTexture;
    }

}
