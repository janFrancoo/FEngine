package model;

public class Texture {

    private final int textureID;
    private float shineDamper = 1;
    private float reflectivity = 0;
    private boolean transparent = false;
    private boolean fakeLight = false;
    private int rows = 1;

    public Texture(int textureID) {
        this.textureID = textureID;
    }

    public int getTextureID() {
        return textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public boolean isFakeLight() {
        return fakeLight;
    }

    public void setFakeLight(boolean fakeLight) {
        this.fakeLight = fakeLight;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

}
